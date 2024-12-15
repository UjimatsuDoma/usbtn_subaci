package prac.tanken.shigure.ui.subaci.ui.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelected
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
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
    private var _playlists = mutableStateOf(emptyList<PlaylistEntity>())
    val playlists get() = _playlists

    // 选中的播放列表在数据库的ID序号。初始值是0；数据库ID自增初始值是1.
    private var _selected = MutableStateFlow(0)
    private val selected = _selected.asStateFlow()

    // 选中的播放列表。
    private var _selectedPlaylistEntity = mutableStateOf<PlaylistEntity?>(null)
    private val selectedPlaylistEntity get() = _selectedPlaylistEntity
    val selectedPlaylistVO
        get() = selectedPlaylistEntity.value?.toSelectionVO()
    val selectedPlaylist
        get() = selectedPlaylistEntity.value?.toPlaylist(voices)

    init {
        loading(Dispatchers.IO) {
            _voices.addAll(resRepository.loadVoices())
        }
        observePlaylists()
        observePlaylistSelection()
    }

    private fun observePlaylists() =
        viewModelScope.launch(Dispatchers.Default) {
            playlistRepository.getAllPlaylists()
                .collect { list ->
                    _playlists.value = list
                }
        }

    private fun observePlaylistSelection() = {
        viewModelScope.launch {
            playlistRepository.getSelected()
                .collect { selected ->
                    val selectedId = selected.selectedId
                    _selected.update { selectedId }
                }
        }
        viewModelScope.launch(Dispatchers.Default) {
            selected.collect {
                updatePlaylistItemsById(it)
            }
        }
    }.invoke()

    private fun updatePlaylistItemsById(id: Int) {
        loading(Dispatchers.IO) {
            _selectedPlaylistEntity.value = null
            val selectedPlaylist = playlistRepository.getById(id)
            _selectedPlaylistEntity.value = selectedPlaylist
        }
    }

    fun selectPlaylist(id: Int) {
        loading(Dispatchers.IO) {
            playlistRepository.selectPlaylist(PlaylistSelected(selectedId = id))
        }
    }

    suspend fun createPlaylist() {
        _selected.value = playlistRepository.testCreatePlaylist()
    }
}