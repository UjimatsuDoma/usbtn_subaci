package prac.tanken.shigure.ui.subaci.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity

@Dao
interface PlaylistDao {
    // 播放列表CRUD
    @Query("SELECT * FROM playlists")
    fun getAll(): Flow<List<PlaylistEntity>>

    @Query("SELECT * FROM playlists WHERE id=:id")
    suspend fun getById(id: Int): PlaylistEntity

    @Query("SELECT MAX(id) FROM playlists")
    suspend fun getMaxId(): Int?

    @Query("SELECT seq FROM sqlite_sequence WHERE name = 'playlists'")
    suspend fun getAutoIncrement(): Int?

    @Query("INSERT INTO playlists (playlist_name, playlist_items) VALUES (:name, '[]')")
    suspend fun createPlaylist(name: String)

    @Update
    suspend fun updatePlaylist(playlistEntity: PlaylistEntity)

    @Delete
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlists WHERE id=:id")
    suspend fun deletePlaylistById(id: Int)
}