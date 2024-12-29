package prac.tanken.shigure.ui.subaci.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelected
import prac.tanken.shigure.ui.subaci.data.model.playlistNotSelected
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
        .map {
            it.filter { it.position == 1 }.toList().let {
                if (it.isNotEmpty()) it[0] else playlistNotSelected
            }
        }

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
    suspend fun getMaxId() = withContext(Dispatchers.IO) {
        return@withContext playlistDao.getMaxId()
    }

    @WorkerThread
    suspend fun getById(id: Long) = withContext(Dispatchers.IO) {
        return@withContext playlistDao.getById(id)
    }

    @WorkerThread
    suspend fun createPlaylist(name: String) = withContext(Dispatchers.IO) {
        playlistDao.createPlaylist(name)
    }

    @WorkerThread
    suspend fun testCreatePlaylist(): Long = withContext(Dispatchers.IO) {
        val maxId = playlistDao.getAutoIncrement()?.let { it + 1 } ?: 1
        playlistDao.createPlaylist("Playlist $maxId")
        return@withContext maxId
    }

    @WorkerThread
    suspend fun getAutoIncrement(): Long? = withContext(Dispatchers.IO) {
        return@withContext playlistDao.getAutoIncrement()
    }

    @WorkerThread
    suspend fun selectPlaylist(playlistSelected: PlaylistSelected) = withContext(Dispatchers.IO) {
        playlistSelectedDao.selectPlaylist(playlistSelected)
    }

    @WorkerThread
    suspend fun playlistExists(name: String) = withContext(Dispatchers.IO) {
        playlistDao.getByName(name).isNotEmpty()
    }

    @WorkerThread
    suspend fun unselectPlaylist() = withContext(Dispatchers.IO) {
        playlistSelectedDao.deleteSelection()
    }

    @WorkerThread
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity) = withContext(Dispatchers.IO) {
        playlistDao.updatePlaylist(playlistEntity)
    }

    @WorkerThread
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity) = withContext(Dispatchers.IO) {
        playlistDao.deletePlaylist(playlistEntity)
    }
}