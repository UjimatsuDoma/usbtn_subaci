package prac.tanken.shigure.ui.subaci.ui.playlist

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelectionVO
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistPlaybackIntent
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistPlaybackSettings
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistPlaybackState
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertError
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertIntent
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertState
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistVO
import prac.tanken.shigure.ui.subaci.ui.playlist.model.playlistNotSelectedVO
import javax.inject.Inject
import prac.tanken.shigure.ui.subaci.R as TankenR

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    val resRepository: ResRepository,
    val playlistUseCase: PlaylistUseCase,
    val myPlayer: MyPlayer,
    val toastUtil: ToastUtil,
) : ViewModel() {
    // 协程相关
    private fun playlistCoroutine(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val playlistViewModelScopeExceptionHandler = CoroutineExceptionHandler { _, _ ->
            playbackState.value = PlaylistPlaybackState.Error
        }
        val context =
            viewModelScope.coroutineContext + dispatcher + playlistViewModelScopeExceptionHandler

        viewModelScope.launch(context) {
            block()
        }
    }

    // 所有播放列表选项
    var playlistSelections = mutableStateOf<List<PlaylistSelectionVO>>(emptyList())
        private set

    // 播放状态
    var playbackState = mutableStateOf<PlaylistPlaybackState>(PlaylistPlaybackState.StandBy)
        private set
    private val _playbackSettings = MutableStateFlow(PlaylistPlaybackSettings())
    val playbackSettings = _playbackSettings.asStateFlow()

    // 创建/修改播放列表相关状态
    private var _upsertState = mutableStateOf<PlaylistUpsertState>(PlaylistUpsertState.Closed)
    val upsertState get() = _upsertState

    init {
        observePlaylists()
        observePlaylistSelection()
        observePlaybackSettings()
    }

    // 启动数据流
    private fun observePlaylists() =
        playlistCoroutine(Dispatchers.Default) {
            playlistUseCase.playlistsFlow
                .collect { playlists ->
                    playlistSelections.value = playlists.map { it.toSelectionVO() }.toList()
                }
        }

    private fun observePlaylistSelection() =
        playlistCoroutine(Dispatchers.Default) {
            playlistUseCase.selectedPlaylist
                .collect { event ->
                    when (event) {
                        is UseCaseEvent.Error -> {
                            toastUtil.toast(event.message)
                            playbackState.value = PlaylistPlaybackState.Error
                        }

                        is UseCaseEvent.Info -> {
                            toastUtil.toast(event.message)
                        }

                        UseCaseEvent.Loading -> {
                            playbackState.value = PlaylistPlaybackState.Loading
                        }

                        is UseCaseEvent.Success<*> -> {
                            playbackState.value =
                                if (event.data is PlaylistVO) {
                                    val selected = event.data
                                    if (selected == playlistNotSelectedVO)
                                        PlaylistPlaybackState.StandBy
                                    else
                                        PlaylistPlaybackState.Loaded.Stopped(selected)
                                } else PlaylistPlaybackState.Error
                        }
                    }
                }
        }

    private fun observePlaybackSettings() =
        playlistCoroutine(Dispatchers.Default) {
            playbackSettings.collect { settings ->
                with(settings) {
                    myPlayer.toggleLooping(looping)
                }
            }
        }

    // 播放状态相关
    /**
     * 点击播放按钮的行为。
     */
    fun dispatchPlaybackIntent(intent: PlaylistPlaybackIntent) {
        when (intent) {
            PlaylistPlaybackIntent.Play -> playList()
            PlaylistPlaybackIntent.Stop -> stop()
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

    /**
     * 播放整个列表。
     */
    fun playList() {
        require(playbackState.value is PlaylistPlaybackState.Loaded.Stopped) {
            "Illegal state: ${playbackState.javaClass.simpleName}"
        }

        val currentState = playbackState.value as PlaylistPlaybackState.Loaded.Stopped
        myPlayer.playByList(
            currentState.playlist.voices.map { VoiceReference(it.id) },
            onStart = { index -> playbackState.value = currentState.play(index) },
            onComplete = { playbackState.value = currentState }
        )
    }

    /**
     * 调整是否循环播放。
     */
    fun toggleLooping() = viewModelScope.launch {
        _playbackSettings.update {
            val oldValue = it.looping
            it.copy(looping = !oldValue)
        }
    }

    /**
     * 停止播放。
     */
    fun stop() {
        require(playbackState.value is PlaylistPlaybackState.Loaded.Playing) {
            "Illegal state: ${playbackState.javaClass.simpleName}"
        }

        val currentState = playbackState.value as PlaylistPlaybackState.Loaded.Playing
        myPlayer.stopIfPlaying()
        playbackState.value = currentState.stop()
    }

    // 播放列表作为整体的操作
    /**
     * 选择数据库中存储的单个播放列表。
     *
     * @param id 播放列表实体在数据库中的主键ID
     */
    fun selectPlaylist(id: Long) {
        require(playbackState.value is PlaylistPlaybackState.Loaded) {
            "Illegal state: ${playbackState.javaClass.simpleName}"
        }

        playlistCoroutine(Dispatchers.IO) {
            playlistUseCase.selectPlaylist(id)
        }
    }

    /**
     * 删除当前选中的播放列表。
     */
    fun deletePlaylist() {
        require(playbackState.value is PlaylistPlaybackState.Loaded) {
            "Illegal state: ${playbackState.value.javaClass.simpleName}"
        }

        playlistCoroutine {
            val actualState = playbackState.value as PlaylistPlaybackState.Loaded
            val event = playlistUseCase.deletePlaylist(actualState.playlist.id)
            if (event is UseCaseEvent.Error) {
                toastUtil.toast(event.message)
            }
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
        var errors = mutableListOf<PlaylistUpsertError>()
        if (draft.name.isEmpty()) {
            errors += PlaylistUpsertError.BlankName
        } else if (playlistUseCase.selectPlaylistByName(draft.name) != null) {
            if (draft.action is PlaylistUpsertIntent.Insert)
                errors += PlaylistUpsertError.ReplicatedName
            else {
                val action = draft.action as PlaylistUpsertIntent.Update
                val playlist = playlistUseCase.selectPlaylistById(action.originalId)
                errors += if (draft.name == playlist.playlistName)
                    PlaylistUpsertError.NameNotChanged
                else PlaylistUpsertError.ReplicatedName
            }
        }
        return draft.copy(errors = errors.toList())
    }

    private suspend fun upsertPlaylist() {
        require(upsertState.value is PlaylistUpsertState.Draft) {
            resRepository.stringRes(TankenR.string.error_illegal_state)
        }

        val draft = upsertState.value as PlaylistUpsertState.Draft
        when (draft.action) {
            PlaylistUpsertIntent.Insert -> playlistUseCase.createPlaylist(draft.name)

            is PlaylistUpsertIntent.Update -> {
                require(playbackState.value is PlaylistPlaybackState.Loaded.Stopped) {
                    resRepository.stringRes(TankenR.string.error_illegal_state)
                }

                val actualState = playbackState.value as PlaylistPlaybackState.Loaded.Stopped
                playlistUseCase.renamePlaylist(actualState.playlist.id, draft.name)
            }
        }
    }

    fun showInsertDialog() = viewModelScope.launch {
        updateUpsertState(
            PlaylistUpsertState.Draft(
                action = PlaylistUpsertIntent.Insert,
                name = resRepository.stringRes(TankenR.string.playlist_new_name_default)
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
        playlistCoroutine {
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
        playlistCoroutine {
            playlistUseCase.removePlaylistItem(
                plistId = actualState.playlist.id,
                index = index,
            )
        }
    }
}