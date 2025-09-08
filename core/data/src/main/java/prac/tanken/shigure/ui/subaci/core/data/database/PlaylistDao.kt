package prac.tanken.shigure.ui.subaci.core.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistEntity

@Dao
interface PlaylistDao {
    // 播放列表CRUD
    @Query("SELECT * FROM playlists")
    fun getAll(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id=:id")
    suspend fun getById(id: Long): PlaylistEntity

    @Query("SELECT * FROM playlists WHERE playlist_name=:name")
    suspend fun getByName(name: String): List<PlaylistEntity>

    @Query("SELECT MAX(id) FROM playlists")
    suspend fun getMaxId(): Long?

    @Query("SELECT seq FROM sqlite_sequence WHERE name = 'playlists'")
    suspend fun getAutoIncrement(): Long?

    @Query("INSERT INTO playlists (playlist_name, playlist_items) VALUES (:name, '[]')")
    suspend fun createPlaylist(name: String): Long

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE id=:id")
    suspend fun deletePlaylistById(id: Long)
}