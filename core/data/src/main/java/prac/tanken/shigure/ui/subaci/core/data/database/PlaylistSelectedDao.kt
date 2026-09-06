package prac.tanken.shigure.ui.subaci.core.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistSelected
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistSelectedEntity

@Dao
interface PlaylistSelectedDao {
    // 选择播放列表
    @Query("SELECT * FROM playlist_selected")
    fun getSelected(): Flow<List<PlaylistSelectedEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun selectPlaylist(playlistSelected: PlaylistSelectedEntity)

    @Query("DELETE FROM playlist_selected WHERE position = 1")
    suspend fun deleteSelection()
}