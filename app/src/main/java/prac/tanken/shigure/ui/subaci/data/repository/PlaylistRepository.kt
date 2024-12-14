package prac.tanken.shigure.ui.subaci.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.data.database.PlaylistDatabase
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    playlistDatabase: PlaylistDatabase,
) {
    val playlistDao = playlistDatabase.playlistDao()

    fun getAllPlaylists() = playlistDao.getAll()

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
}