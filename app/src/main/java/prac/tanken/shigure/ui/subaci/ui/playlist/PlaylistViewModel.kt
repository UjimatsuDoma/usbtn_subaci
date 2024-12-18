package prac.tanken.shigure.ui.subaci.ui.playlist

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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
    private var _playlists = mutableStateOf<List<PlaylistEntity>>(emptyList())
    private val playlists get() = _playlists
    val playlistsSelections get() = playlists.value.map { it.toSelectionVO() }.toList()

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
                .collect {
                    _playlists.value = it
                }
        }

    private fun observePlaylistSelection() =
        viewModelScope.launch(Dispatchers.Default) {
            playlistRepository.selectedPlaylist
                .collect { _selectedPlaylistEntity.value = it }
        }

    fun selectPlaylist(id: Int) {
        loading(Dispatchers.IO) {
            playlistRepository.selectPlaylist(PlaylistSelected(selectedId = id))
        }
    }

    suspend fun createPlaylist() {
        val createdId = playlistRepository.testCreatePlaylist()
        selectPlaylist(createdId)
    }
}