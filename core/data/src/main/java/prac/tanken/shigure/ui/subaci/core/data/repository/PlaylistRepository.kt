package prac.tanken.shigure.ui.subaci.core.data.repository

import androidx.annotation.WorkerThread
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.core.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.core.data.model.Playlist
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistSelected
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistSelectedEntity
import prac.tanken.shigure.ui.subaci.core.data.model.playlistNotSelected
import javax.inject.Inject

class PlaylistRepository(
    val playlistDatabase: PlaylistDatabase,
) {
    private val playlistDao = playlistDatabase.playlistDao()
    private val playlistSelectedDao = playlistDatabase.playlistSelectedDao()

    // 所有播放列表数据的流
    private val playlistEntitiesFlow = playlistDao.getAll()
    val playlistsFlow: Flow<RepositoryEvent<List<Playlist>, Exception>> =
        playlistEntitiesFlow
            .transform { entities ->
                try {
                    emit(RepositoryEvent.Working)
                    val playlists = entities.map {
                        it.convertToPlaylist()
                    }
                    emit(RepositoryEvent.Success(playlists))
                } catch (e: Exception) {
                    emit(RepositoryEvent.Error(e))
                }
            }

    // 播放列表选择项数据的流
    private val playlistSelectedFlow = playlistSelectedDao.getSelected()

    val selectedPlaylistFlow: Flow<RepositoryEvent<Playlist?, Exception>> =
        combine(playlistsFlow, playlistSelectedFlow) { f1, f2 ->
            f1 to f2.first().toPlaylistSelected()
        }.transform { (event, playlistSelected) ->
            when (event) {
                RepositoryEvent.Working -> {
                    emit(RepositoryEvent.Working)
                }

                is RepositoryEvent.Error -> {
                    emit(RepositoryEvent.Error(event.error))
                }

                is RepositoryEvent.Success -> {
                    val allPlaylists = event.data
                    val selectedPlaylist = allPlaylists.firstOrNull {
                        it.id == playlistSelected.selectedId
                    }
                    emit(RepositoryEvent.Success(selectedPlaylist))
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
        playlistSelectedDao.selectPlaylist(PlaylistSelectedEntity(id))
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