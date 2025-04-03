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

class PlaylistRepository @Inject constructor(
    playlistDatabase: PlaylistDatabase,
) {
    val playlistDao = playlistDatabase.playlistDao()
    val playlistSelectedDao = playlistDatabase.playlistSelectedDao()

    // 所有播放列表数据的流
    val playlistsFlow = playlistDao.getAll()
    // 播放列表选择项数据的流
    val playlistSelectedFlow = playlistSelectedDao.getSelected()
        .map {
            if(it.isNotEmpty()) it.first()
            else playlistNotSelected
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
    suspend fun getByName(name: String) = withContext(Dispatchers.IO) {
        return@withContext playlistDao.getByName(name)
    }

    @WorkerThread
    suspend fun createPlaylist(name: String) = withContext(Dispatchers.IO) {
        playlistDao.createPlaylist(name)
    }

    @WorkerThread
    suspend fun getAutoIncrement(): Long? = withContext(Dispatchers.IO) {
        return@withContext playlistDao.getAutoIncrement()
    }

    @WorkerThread
    suspend fun selectPlaylist(id: Long) = withContext(Dispatchers.IO) {
        playlistSelectedDao.selectPlaylist(PlaylistSelected(id))
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