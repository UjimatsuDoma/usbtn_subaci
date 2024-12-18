package prac.tanken.shigure.ui.subaci.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelected
import javax.inject.Inject
import kotlin.collections.filter
import kotlin.collections.isNotEmpty

class PlaylistRepository @Inject constructor(
    playlistDatabase: PlaylistDatabase,
) {
    val playlistDao = playlistDatabase.playlistDao()
    val playlistSelectedDao = playlistDatabase.playlistSelectedDao()

    fun getAllPlaylists() = playlistDao.getAll()
    fun getSelected() = playlistSelectedDao.getSelected()
        .map { it.filter { it.position == 1 }.toList()[0] }

    val selectedPlaylist = getAllPlaylists()
        .combineTransform(getSelected()) { playlists, selected ->
            if (playlists.isNotEmpty()) {
                val playlistsFiltered = playlists.filter { it.id == selected.selectedId }
                if (playlistsFiltered.isNotEmpty()) {
                    val selectedPlaylist = playlistsFiltered[0]
                    emit(selectedPlaylist)
                }
            }
        }

    @WorkerThread
    suspend fun getById(id: Int) = withContext(Dispatchers.IO) {
        return@withContext playlistDao.getById(id)
    }

    @WorkerThread
    suspend fun createPlaylist(name: String) = withContext(Dispatchers.IO) {
        playlistDao.createPlaylist(name)
    }

    @WorkerThread
    suspend fun testCreatePlaylist(): Int = withContext(Dispatchers.IO) {
        val maxId = playlistDao.getMaxId()?.let { it + 1 } ?: 1
        playlistDao.createPlaylist("Playlist $maxId")
        return@withContext maxId
    }

    @WorkerThread
    suspend fun selectPlaylist(playlistSelected: PlaylistSelected) = withContext(Dispatchers.IO) {
        playlistSelectedDao.selectPlaylist(playlistSelected)
    }

    @WorkerThread
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity) = withContext(Dispatchers.IO) {
        playlistDao.updatePlaylist(playlistEntity)
    }
}