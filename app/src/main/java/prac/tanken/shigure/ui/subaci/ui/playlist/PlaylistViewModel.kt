package prac.tanken.shigure.ui.subaci.ui.playlist

import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.data.model.Playlist
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelected
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelectionVO
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistPlaybackIntent
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistPlaybackState
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertError
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertIntent
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertState
import prac.tanken.shigure.ui.subaci.R as TankenR
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    val resRepository: ResRepository,
    val playlistRepository: PlaylistRepository,
    val toastUtil: ToastUtil,
    val myPlayer: MyPlayer
) : LoadingViewModel() {
    // 全部语音数据。一次加载，永不更改。
    private val _voices = mutableStateListOf<Voice>()
    private val voices get() = _voices

    // 全部播放列表数据。CRUD完备。
    private var _playlists = mutableStateOf<List<PlaylistEntity>>(emptyList())
    private val playlists get() = _playlists
    val playlistsSelections = derivedStateOf { playlists.value.map { it.toSelectionVO() }.toList() }

    // 选中的播放列表。
    private var _selectedPlaylistEntity = mutableStateOf<PlaylistEntity?>(null)
    private val selectedPlaylistEntity get() = _selectedPlaylistEntity
    val selectedPlaylist = derivedStateOf { selectedPlaylistEntity.value?.toPlaylist(voices) }

    // 播放状态
    private var _playbackState =
        MutableStateFlow<PlaylistPlaybackState>(PlaylistPlaybackState.Stopped)
    val playbackState = _playbackState.asStateFlow()
    private var _isLooping = MutableStateFlow(false)
    val isLooping = _isLooping.asStateFlow()

    // 创建/修改播放列表相关状态
    private var _upsertState = MutableStateFlow<PlaylistUpsertState>(PlaylistUpsertState.Closed)
    val upsertState = _upsertState.asStateFlow()

    init {
        loading(Dispatchers.IO) {
            _voices.addAll(resRepository.loadVoices())
        }
        observePlaylists()
        observePlaylistSelection()
        observeIsLooping()
    }

    private fun observePlaylists() =
        viewModelScope.launch(Dispatchers.Default) {
            playlistRepository.getAllPlaylists()
                .collect {
                    _playlists.value = it
                }
        }

    private fun observePlaylistSelection() =
        viewModelScope.launch(Dispatchers.Default) {
            playlistRepository.selectedPlaylist
                .collect { _selectedPlaylistEntity.value = it }
        }

    private fun observeIsLooping() =
        viewModelScope.launch(Dispatchers.Default) {
            isLooping.collect { looping ->
                _playbackState.update { state ->
                    when (state) {
                        is PlaylistPlaybackState.Playing -> state.copy(looping = looping)
                        PlaylistPlaybackState.Stopped -> state
                    }
                }
                myPlayer.toggleLooping(looping)
            }
        }

    fun dispatchPlaybackIntent(intent: PlaylistPlaybackIntent) {
        when (intent) {
            PlaylistPlaybackIntent.Play -> playList()
            PlaylistPlaybackIntent.Stop -> stop()
        }
    }

    fun playItem(index: Int) =
        myPlayer.playByReference(
            voices.filter { it.id == selectedPlaylist.value?.playlistItems[index]?.id }[0].toReference(),
            onStart = { _playbackState.update { PlaylistPlaybackState.Playing(index) } },
            onComplete = { _playbackState.update { PlaylistPlaybackState.Stopped } }
        )

    fun playList() =
        selectedPlaylist.value?.let { playlist ->
            myPlayer.playByList(
                playlist.playlistItems.map { it.toReference() },
                onStart = { index -> _playbackState.update { PlaylistPlaybackState.Playing(index) } },
                onComplete = { _playbackState.update { PlaylistPlaybackState.Stopped } },
            )
        }

    fun toggleLooping() = viewModelScope.launch {
        _isLooping.update { !it }
    }

    fun stop() {
        myPlayer.stopIfPlaying()
        _playbackState.update { PlaylistPlaybackState.Stopped }
    }

    fun selectPlaylist(id: Int) {
        loading(Dispatchers.IO) {
            playlistRepository.selectPlaylist(PlaylistSelected(id))
        }
    }

    fun unselectPlaylist() {
        loading(Dispatchers.IO) {
            playlistRepository.unselectPlaylist()
        }
    }

    suspend fun createPlaylist() {
        val createdId = playlistRepository.testCreatePlaylist()
        selectPlaylist(createdId)
    }

    suspend fun createPlaylist(name: String) {
        val createdId = playlistRepository.createPlaylist(name)
        selectPlaylist(createdId.toInt())
    }

    suspend fun updateUpsertState(playlistUpsertState: PlaylistUpsertState) =
        when (playlistUpsertState) {
            PlaylistUpsertState.Closed -> _upsertState.update { playlistUpsertState }
            is PlaylistUpsertState.Draft -> {
                val draft = playlistUpsertState
                var errors: List<PlaylistUpsertError> = emptyList()
                if (draft.name.isEmpty()) {
                    errors = errors.toMutableList().apply {
                        add(PlaylistUpsertError.BlankName)
                    }.toList()
                } else if (playlistRepository.playlistExists(draft.name)) {
                    errors = errors.toMutableList().apply {
                        add(PlaylistUpsertError.ReplicatedName)
                    }.toList()
                }
                _upsertState.update { draft.copy(errors = errors) }
            }
        }

    private suspend fun upsertPlaylist() {
        require(upsertState.value is PlaylistUpsertState.Draft) {
            resRepository.stringRes(TankenR.string.error_illegal_state)
        }

        val draft = upsertState.value as PlaylistUpsertState.Draft
        when (draft.action) {
            PlaylistUpsertIntent.Insert -> createPlaylist(draft.name)
            is PlaylistUpsertIntent.Update -> selectedPlaylistEntity.value?.let { playlist ->
                playlistRepository.updatePlaylist(playlist.copy(playlistName = draft.name))
            }
        }
    }

    fun showInsertDialog() = viewModelScope.launch() {
        updateUpsertState(
            PlaylistUpsertState.Draft(
                action = PlaylistUpsertIntent.Insert,
                name = "New Playlist"
            )
        )
    }

    fun showUpdateDialog(vo: PlaylistSelectionVO) = viewModelScope.launch() {
        updateUpsertState(
            PlaylistUpsertState.Draft(
                action = PlaylistUpsertIntent.Update(
                    originalId = vo.id
                ),
                name = vo.playlistName
            )
        )
    }

    fun submitUpsert() = viewModelScope.launch(Dispatchers.Default) {
        upsertPlaylist()
        updateUpsertState(PlaylistUpsertState.Closed)
    }

    fun cancelUpsert() = viewModelScope.launch {
        updateUpsertState(PlaylistUpsertState.Closed)
    }

    suspend fun deletePlaylist() = selectedPlaylistEntity.value?.let {
        withContext(Dispatchers.IO) {
            unselectPlaylist()
            playlistRepository.deletePlaylist(it)
            playlistRepository.getMaxId()?.let { selectPlaylist(it) }
        }
    }

    fun movePlaylistItem(index: Int, moveUp: Boolean) =
        selectedPlaylistEntity.value?.let { playlist ->
            val items = parseJsonString<List<String>>(playlist.playlistItems)

            require(index in items.indices) {
                resRepository.stringRes(TankenR.string.error_illegal_index)
            }
            val moveUpValid = moveUp && index in 1..items.lastIndex
            val moveDownValid = !moveUp && index in 0 until items.lastIndex
            require(moveUpValid || moveDownValid) {
                resRepository.stringRes(TankenR.string.error_playlist_move_item_oob)
            }

            val targetIndex = if (moveUp) index - 1 else index + 1
            val newArr = items.toMutableList().also { arr ->
                arr[index] = arr[targetIndex].apply {
                    arr[targetIndex] = arr[index]
                }
            }.toList()
            val newArrStr = Json.encodeToString(newArr)
            viewModelScope.launch(Dispatchers.Default) {
                playlistRepository.updatePlaylist(playlist.copy(playlistItems = newArrStr))
            }
        }

    fun removePlaylistItem(index: Int) =
        selectedPlaylistEntity.value?.let { playlist ->
            val items = parseJsonString<List<String>>(playlist.playlistItems)

            require(index in items.indices) {
                resRepository.stringRes(TankenR.string.error_illegal_index)
            }

            val newArr = items.toMutableList().apply { removeAt(index) }.toList()
            val newArrStr = Json.encodeToString(newArr)
            viewModelScope.launch(Dispatchers.Default) {
                playlistRepository.updatePlaylist(playlist.copy(playlistItems = newArrStr))
            }
        }
}