package prac.tanken.shigure.ui.subaci.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistSelected
import prac.tanken.shigure.ui.subaci.core.data.model.PlaylistSelectedEntity

@Database(entities = [PlaylistEntity::class, PlaylistSelectedEntity::class], version = 1)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistSelectedDao(): PlaylistSelectedDao
}