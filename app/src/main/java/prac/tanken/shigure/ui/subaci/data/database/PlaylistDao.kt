package prac.tanken.shigure.ui.subaci.data.database

import androidx.room.Dao
import androidx.room.Query
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity

@Dao
interface PlaylistDao {
    @Query("SELECT * FROM playlists")
    fun getAll(): List<PlaylistEntity>
}