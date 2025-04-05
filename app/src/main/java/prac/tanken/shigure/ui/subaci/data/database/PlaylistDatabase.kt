package prac.tanken.shigure.ui.subaci.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelected

@Database(entities = [PlaylistEntity::class, PlaylistSelected::class], version = 1)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistSelectedDao(): PlaylistSelectedDao
}