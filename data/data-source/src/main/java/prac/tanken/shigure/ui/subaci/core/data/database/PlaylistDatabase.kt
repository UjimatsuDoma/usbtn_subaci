package prac.tanken.shigure.ui.subaci.core.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import prac.tanken.shigure.ui.subaci.core.data.model.playlist.PlaylistEntity
import prac.tanken.shigure.ui.subaci.core.data.model.playlist.PlaylistSelection

@Database(
    entities = [PlaylistEntity::class, PlaylistSelection::class],
    version = 2,
)
abstract class PlaylistDatabase : RoomDatabase() {
    abstract fun playlistDao(): PlaylistDao
    abstract fun playlistSelectedDao(): PlaylistSelectedDao
}