package prac.tanken.shigure.ui.subaci.feature.playlist

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.core.data.model.playlistNotSelected
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.RepositoryEvent
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.feature.base.mvi.BaseViewModel
import prac.tanken.shigure.ui.subaci.feature.playlist.domain.PlaylistUseCase
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
            observePlaylists()
        }
        viewModelScope.launch(Dispatchers.IO) {
            observePlaylistSelected()
        }
    }

    private suspend fun observePlaylists() =
        playlistRepository.playlistsFlow
            .collect { event ->
                when (event) {
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
                                    message = event.error.message
                                        ?: event.error.stackTraceToString()
                                )
                            )
                        }
                    }

                    is RepositoryEvent.Success -> {
                        setState {
                            copy(
                                playlistsUiState = PlaylistContract.PlaylistsUiState.Loaded(
                                    playlists = event.data
                                )
                            )
                        }
                    }
                }
            }

    private suspend fun observePlaylistSelected() =
        playlistRepository.selectedPlaylistFlow
            .collect { event ->
                when (event) {
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
                                    message = event.error.message
                                        ?: event.error.stackTraceToString()
                                )
                            )
                        }
                    }

                    is RepositoryEvent.Success -> {
                        event.data?.let { selectedPlaylist ->
                            val allVoices = voicesRepository.voicesMetadata
                            val selectedVoices = selectedPlaylist.playlistItemIds
                                .mapNotNull { playlistItemId ->
                                    allVoices?.firstOrNull { it.id == playlistItemId }
                                }
                            setState {
                                copy(
                                    playlistUiState = PlaylistContract.PlaylistUiState.Loaded(
                                        selectedPlaylistIndex = selectedPlaylist.id,
                                        voices = selectedVoices
                                    )
                                )
                            }
                        } ?: setState {
                            copy(
                                playlistUiState = PlaylistContract.PlaylistUiState.Loaded(
                                    selectedPlaylistIndex = playlistNotSelected.selectedId,
                                    voices = emptyList()
                                )
                            )
                        }
                    }
                }
            }

    // convenient functions
    private fun <R> requirePlaylistsLoaded(
        block: (PlaylistContract.PlaylistsUiState.Loaded)->R
    ) {
        val playlistsUiState = state.value.playlistsUiState
        require(playlistsUiState is PlaylistContract.PlaylistsUiState.Loaded) {
            "Illegal state: ${state.value.playlistsUiState.javaClass.simpleName}"
        }

        block(playlistsUiState)
    }
    private fun <R> requirePlaylistSelected(
        block: (PlaylistContract.PlaylistsUiState.Loaded, PlaylistContract.PlaylistUiState.Loaded) -> R
    ) {
        val playlistsUiState = state.value.playlistsUiState
        val playlistUiState = state.value.playlistUiState
        require(playlistsUiState is PlaylistContract.PlaylistsUiState.Loaded) {
            "Illegal state: ${state.value.playlistsUiState.javaClass.simpleName}"
        }
        require(playlistUiState is PlaylistContract.PlaylistUiState.Loaded) {
            "Illegal state: ${state.value.playlistUiState.javaClass.simpleName}"
        }

        block(playlistsUiState, playlistUiState)
    }
    private fun <R> operateOnSelectedPlaylist(
        block: (PlaylistEntity)->R
    ) = requirePlaylistSelected { playlistsUiState, playlistUiState ->
        val selectedPlaylistEntity = playlistsUiState.playlists
            .firstOrNull { it.id == playlistUiState.selectedPlaylistIndex }
            ?.toEntity()
        selectedPlaylistEntity?.let(block)
            ?: sendEffect(
                PlaylistContract.Effect.ShowSnackbar(
                    resRepository.stringRes(R.string.playlist_info_no_selected)
                )
            )
    }

    /**
     * 创建/修改播放列表相关状态
     */
    private var _upsertState = MutableStateFlow<PlaylistUpsertState>(PlaylistUpsertState.Closed)
    val upsertState = _upsertState.asStateFlow()

    /**
     * 播放单个项目。
     *
     * @param index 该项目在播放列表中的序号
     */
    fun playItem(index: Int) = requirePlaylistSelected { _, playlist->
        myPlayer.playByReference(
            VoiceReference(playlist.voices[index].id),
        )
    }

    // 播放列表作为整体的操作
    /**
     * 选择数据库中存储的单个播放列表。
     *
     * @param id 播放列表实体在数据库中的主键ID
     */
    fun selectPlaylist(id: Long) = requirePlaylistsLoaded {
        viewModelScope.launch(Dispatchers.IO) {
            playlistRepository.selectPlaylist(id)
        }
    }

    /**
     * 删除当前选中的播放列表。
     */
    fun deletePlaylist() =
        operateOnSelectedPlaylist { playlistEntity ->
            viewModelScope.launch(Dispatchers.Default) {
                playlistRepository.unselectPlaylist()
                playlistRepository.deletePlaylist(playlistEntity)
                playlistRepository.getMaxId()?.let { selectPlaylist(it) }
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
        val errors = buildList {
            if (draft.name.isEmpty()) {
                add(PlaylistUpsertError.BlankName)
            } else if (playlistRepository.getByName(draft.name).isNotEmpty()) {
                if (draft.action is PlaylistUpsertIntent.Insert)
                    add(PlaylistUpsertError.ReplicatedName)
                else {
                    val action = draft.action as PlaylistUpsertIntent.Update
                    val playlist = playlistRepository.getById(action.originalId)
                    add(
                        if (draft.name == playlist.playlistName)
                            PlaylistUpsertError.NameNotChanged
                        else PlaylistUpsertError.ReplicatedName
                    )
                }
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

            is PlaylistUpsertIntent.Update ->
                operateOnSelectedPlaylist { playlistEntity ->
                    viewModelScope.launch(Dispatchers.Default) {
                        playlistRepository.updatePlaylist(
                            playlistEntity.copy(playlistName = draft.name)
                        )
                    }
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

    fun showUpdateDialog() =
        operateOnSelectedPlaylist { playlistEntity ->
            viewModelScope.launch(Dispatchers.IO) {
                updateUpsertState(
                    PlaylistUpsertState.Draft(
                        action = PlaylistUpsertIntent.Update(
                            originalId = playlistEntity.id
                        ),
                        name = playlistEntity.playlistName
                    )
                )
            }
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
    fun movePlaylistItem(index: Int, moveUp: Boolean) =
        operateOnSelectedPlaylist { playlistEntity ->
            viewModelScope.launch(Dispatchers.IO) {
                playlistUseCase.movePlaylistItem(
                    plistId = playlistEntity.id,
                    index = index,
                    moveUp = moveUp
                )
            }
        }

    fun removePlaylistItem(index: Int) =
        operateOnSelectedPlaylist { playlistEntity ->
            viewModelScope.launch(Dispatchers.IO) {
                playlistUseCase.removePlaylistItem(
                    plistId = playlistEntity.id,
                    index = index,
                )
            }
        }
}