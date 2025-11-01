package prac.tanken.shigure.ui.subaci.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistSelected

@Dao
interface PlaylistSelectedDao {
    // 选择播放列表
    @Query("SELECT * FROM playlist_selected")
    fun getSelected(): Flow<List<PlaylistSelected>>

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun selectPlaylist(playlistSelected: PlaylistSelected)

    @Query("DELETE FROM playlist_selected WHERE position = 1")
    suspend fun deleteSelection()
}