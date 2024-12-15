package prac.tanken.shigure.ui.subaci.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelected
import javax.inject.Inject

class PlaylistRepository @Inject constructor(
    playlistDatabase: PlaylistDatabase,
) {
    val playlistDao = playlistDatabase.playlistDao()
    val playlistSelectedDao = playlistDatabase.playlistSelectedDao()

    fun getAllPlaylists() = playlistDao.getAll()
    fun getSelected() = playlistSelectedDao.getSelected()
        .onEach { println(it) }
        .map { it.filter { it.position == 1 }.toList()[0] }

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
}