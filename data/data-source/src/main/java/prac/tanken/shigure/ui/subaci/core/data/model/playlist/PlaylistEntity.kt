package prac.tanken.shigure.ui.subaci.core.data.model.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlists",
    indices = [Index(value = ["playlist_name"], unique = true)]
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "playlist_name") val playlistName: String,
    @ColumnInfo(name = "playlist_items") val playlistItems: String,
)