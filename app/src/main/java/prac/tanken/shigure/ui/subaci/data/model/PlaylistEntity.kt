package prac.tanken.shigure.ui.subaci.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString

@Entity(tableName = "playlists")
data class PlaylistEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "playlist_name") val playlistName: String,
    @ColumnInfo(name = "playlist_items") val playlistItems: String,
) {
    fun toPlaylist(voices: List<Voice>) = Playlist(
        playlistName = playlistName,
        playlistItems = voices.filter {
            it.id in parseJsonString<List<String>>(playlistItems)
        }.toList()
    )
}

data class Playlist(
    val playlistName: String,
    val playlistItems: List<Voice>,
)
