package prac.tanken.shigure.ui.subaci.feature.playlist

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.model.playlistNotSelected
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.RepositoryEvent
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.feature.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.feature.base.mvi.BaseViewModel
import prac.tanken.shigure.ui.subaci.feature.playlist.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistPlaybackSettings
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistPlaybackState
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertError
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertIntent
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertState
import javax.inject.Inject
import prac.tanken.shigure.ui.subaci.core.common.R as CommonR

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    val resRepository: ResRepository,
    val playlistUseCase: PlaylistUseCase,
    val playlistRepository: PlaylistRepository,
    val voicesRepository: VoicesRepository,
    val myPlayer: MyPlayer,
) : BaseViewModel<PlaylistContract.State, PlaylistContract.Intent, PlaylistContract.Effect>() {

    override fun initState(): PlaylistContract.State = PlaylistContract.State()

    override fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            observePlaylistsAndSelection()
        }
        viewModelScope.launch(Dispatchers.IO) {
            observePlaybackSettings()
        }
    }

    private suspend fun observePlaylistsAndSelection() =
        combineTransform(
            playlistRepository.playlistSelectedFlow,
            playlistRepository.playlistsFlow
        ) { f1, f2 ->
            println("combx $f1, $f2")
            emit(Pair(f1, f2))
        }
            .collect { (playlistSelected, playlists) ->
                println("collect $playlistSelected $playlists")
                println(state.value)
                when (playlists) {
                    RepositoryEvent.Working -> {
                        setState {
                            copy(
                                playlistsUiState = PlaylistContract.PlaylistsUiState.Loading
                            )
                        }
                    }

                    is RepositoryEvent.Error -> {
                        setState {
                            copy(
                                playlistsUiState = PlaylistContract.PlaylistsUiState.Error(
                                    playlists.error.message ?: playlists.error.stackTraceToString()
                                )
                            )
                        }
                    }

                    is RepositoryEvent.Success -> {
                        setState {
                            copy(
                                playlistsUiState = PlaylistContract.PlaylistsUiState.Loaded(
                                    playlists.data
                                )
                            )
                        }

                        when (playlistSelected) {
                            RepositoryEvent.Working -> {
                                setState {
                                    copy(
                                        playlistUiState = PlaylistContract.PlaylistUiState.Loading
                                    )
                                }
                            }

                            is RepositoryEvent.Error -> {
                                setState {
                                    copy(
                                        playlistUiState = PlaylistContract.PlaylistUiState.Error(
                                            playlistSelected.error.message
                                                ?: playlistSelected.error.stackTraceToString()
                                        )
                                    )
                                }
                            }

                            is RepositoryEvent.Success -> {
                                val allVoices = voicesRepository.voicesMetadata
                                    ?: error("Voices metadata is not loaded yet.")
                                val allPlaylists = playlists.data
                                if (playlistSelected.data == playlistNotSelected) {
                                    setState {
                                        copy(
                                            playlistUiState = PlaylistContract.PlaylistUiState.Loaded(
                                                selectedPlaylistIndex = playlistSelected.data.selectedId,
                                                voices = emptyList()
                                            )
                                        )
                                    }
                                } else {
                                    val selectedPlaylist = allPlaylists.firstOrNull {
                                        it.id == playlistSelected.data.selectedId
                                    }
                                    selectedPlaylist?.let {
                                        val selectedVoices = allVoices.filter {
                                            it.id in selectedPlaylist.playlistItemIds
                                        }
                                        setState {
                                            copy(
                                                playlistUiState = PlaylistContract.PlaylistUiState.Loaded(
                                                    selectedPlaylistIndex = playlistSelected.data.selectedId,
                                                    voices = selectedVoices
                                                )
                                            )
                                        }
                                    } ?: run {
                                        setState {
                                            copy(
                                                playlistUiState = PlaylistContract.PlaylistUiState.Error(
                                                    "Cannot find playlist with id ${playlistSelected.data.selectedId}"
                                                )
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

    // 播放状态
    var playbackState = mutableStateOf<PlaylistPlaybackState>(PlaylistPlaybackState.StandBy)
        private set
    private val _playbackSettings = MutableStateFlow(PlaylistPlaybackSettings())
    val playbackSettings = _playbackSettings.asStateFlow()

    // 创建/修改播放列表相关状态
    private var _upsertState = mutableStateOf<PlaylistUpsertState>(PlaylistUpsertState.Closed)
    val upsertState get() = _upsertState

    private suspend fun observePlaybackSettings(): Nothing =
        playbackSettings.collect { settings ->
            with(settings) {
                myPlayer.toggleLooping(looping)
            }
        }

    /**
     * 播放单个项目。
     *
     * @param index 该项目在播放列表中的序号
     */
    fun playItem(index: Int) {
        require(playbackState.value is PlaylistPlaybackState.Loaded.Stopped) {
            "Illegal state: ${playbackState.javaClass.simpleName}"
        }

        val currentState = playbackState.value as PlaylistPlaybackState.Loaded.Stopped
        myPlayer.playByReference(
            VoiceReference(currentState.playlist.voices[index].id),
            onStart = { playbackState.value = currentState.play(index) },
            onComplete = { playbackState.value = currentState }
        )
    }

    // 播放列表作为整体的操作
    /**
     * 选择数据库中存储的单个播放列表。
     *
     * @param id 播放列表实体在数据库中的主键ID
     */
    fun selectPlaylist(id: Long) {
        require(state.value.playlistsUiState is PlaylistContract.PlaylistsUiState.Loaded) {
            "Illegal state: ${state.value.playlistsUiState.javaClass.simpleName}"
        }

        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.selectPlaylist(id)
        }
    }

    /**
     * 删除当前选中的播放列表。
     */
    suspend fun deletePlaylist() {
        require(playbackState.value is PlaylistPlaybackState.Loaded) {
            "Illegal state: ${playbackState.value.javaClass.simpleName}"
        }

        val actualState = playbackState.value as PlaylistPlaybackState.Loaded
        val event = playlistUseCase.deletePlaylist(actualState.playlist.id)
        if (event is UseCaseEvent.Error) {
            Log.d(this@PlaylistViewModel::class.simpleName, event.message)
        }
    }

    /**
     * 更改添加或重命名播放列表对话框的界面状态。
     * 目前的缺陷是：仅能根据打开、关闭管理，不能区分是添加还是重命名。
     *
     * @param playlistUpsertState 前述对话框的界面状态的目标值
     */
    fun updateUpsertState(
        playlistUpsertState: PlaylistUpsertState
    ) {
        when (playlistUpsertState) {
            PlaylistUpsertState.Closed -> _upsertState.value = playlistUpsertState
            is PlaylistUpsertState.Draft -> {
                // 重点：文本框的值更新必须同步，否则会导致输入文本错乱。
                _upsertState.value = playlistUpsertState
                // 检查错误
                viewModelScope.launch {
                    val validatedDraft = validateUpsertDraft(playlistUpsertState)
                    _upsertState.value = validatedDraft
                }
            }
        }
    }

    private suspend fun validateUpsertDraft(
        draft: PlaylistUpsertState.Draft
    ): PlaylistUpsertState.Draft {
        val errors = mutableListOf<PlaylistUpsertError>()
        if (draft.name.isEmpty()) {
            errors + PlaylistUpsertError.BlankName
        } else if (playlistRepository.getByName(draft.name).isNotEmpty()) {
            if (draft.action is PlaylistUpsertIntent.Insert)
                errors + PlaylistUpsertError.ReplicatedName
            else {
                val action = draft.action as PlaylistUpsertIntent.Update
                val playlist = playlistRepository.getById(action.originalId)
                errors += if (draft.name == playlist.playlistName)
                    PlaylistUpsertError.NameNotChanged
                else PlaylistUpsertError.ReplicatedName
            }
        }
        return draft.copy(errors = errors.toList())
    }

    private suspend fun upsertPlaylist() {
        require(upsertState.value is PlaylistUpsertState.Draft) {
            resRepository.stringRes(CommonR.string.error_illegal_state)
        }

        val draft = upsertState.value as PlaylistUpsertState.Draft
        when (draft.action) {
            PlaylistUpsertIntent.Insert -> {
                val createdId = playlistRepository.createPlaylist(draft.name)
                selectPlaylist(createdId)
            }

            is PlaylistUpsertIntent.Update -> {
                require(playbackState.value is PlaylistPlaybackState.Loaded.Stopped) {
                    resRepository.stringRes(CommonR.string.error_illegal_state)
                }

                val actualState = playbackState.value as PlaylistPlaybackState.Loaded.Stopped
                val entity = playlistRepository.getById(actualState.playlist.id)
                playlistRepository.updatePlaylist(entity.copy(playlistName = draft.name))
            }
        }
    }

    fun showInsertDialog() = viewModelScope.launch {
        updateUpsertState(
            PlaylistUpsertState.Draft(
                action = PlaylistUpsertIntent.Insert,
                name = resRepository.stringRes(R.string.playlist_new_name_default)
            )
        )
    }

    fun showUpdateDialog(playbackState: PlaylistPlaybackState.Loaded) = viewModelScope.launch {
        val selectedPlaylist = playbackState.playlist
        updateUpsertState(
            PlaylistUpsertState.Draft(
                action = PlaylistUpsertIntent.Update(
                    originalId = selectedPlaylist.id
                ),
                name = selectedPlaylist.playlistName
            )
        )
    }

    fun submitUpsert() = viewModelScope.launch {
        upsertPlaylist()
        updateUpsertState(PlaylistUpsertState.Closed)
    }

    fun cancelUpsert() = viewModelScope.launch {
        updateUpsertState(PlaylistUpsertState.Closed)
    }

    // 播放列表内部项目的操作
    /**
     * 移动播放列表内的单个项目。
     * 目前仅支持跟前面或后面一个项目交换位置。
     *
     * @param index 要移动的项目在播放列表内部的序号
     * @param moveUp 指定是向上还是向下移动，为true表示向上。
     */
    fun movePlaylistItem(index: Int, moveUp: Boolean) {
        if (playbackState.value !is PlaylistPlaybackState.Loaded.Stopped) {
            val message = "Illegal state: ${playbackState.javaClass.simpleName}"
            throw IllegalStateException(message)
        }

        val actualState = playbackState.value as PlaylistPlaybackState.Loaded.Stopped
        viewModelScope.launch(Dispatchers.IO) {
            playlistUseCase.movePlaylistItem(
                plistId = actualState.playlist.id,
                index = index,
                moveUp = moveUp
            )
        }
    }

    fun removePlaylistItem(index: Int) {
        if (playbackState.value !is PlaylistPlaybackState.Loaded.Stopped) {
            val message = "Illegal state: ${playbackState.javaClass.simpleName}"
            throw IllegalStateException(message)
        }

        val actualState = playbackState.value as PlaylistPlaybackState.Loaded.Stopped
        viewModelScope.launch(Dispatchers.IO) {
            playlistUseCase.removePlaylistItem(
                plistId = actualState.playlist.id,
                index = index,
            )
        }
    }
}