package prac.tanken.shigure.ui.subaci.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import prac.tanken.shigure.ui.subaci.core.data.model.playlist.PlaylistSelection

@Dao
interface PlaylistSelectedDao {
    // 选择播放列表
    @Query("SELECT * FROM playlist_selection")
    fun getSelected(): Flow<List<PlaylistSelection>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun selectPlaylist(playlistSelected: PlaylistSelection)

    @Query("DELETE FROM playlist_selection WHERE position = 1")
    suspend fun deleteSelection()
}