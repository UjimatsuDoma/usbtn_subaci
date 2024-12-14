package prac.tanken.shigure.ui.subaci.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString

@Entity(
    tableName = "playlists",
    indices = [Index(value = ["playlist_name"], unique = true)]
)
data class PlaylistEntity(
    @ColumnInfo(name = "playlist_name") val playlistName: String,
    @ColumnInfo(name = "playlist_items") val playlistItems: String,
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0

    fun toPlaylist(voices: List<Voice>) = Playlist(
        id = id,
        playlistName = playlistName,
        playlistItems = voices.filter {
            it.id in parseJsonString<List<String>>(playlistItems)
        }.toList()
    )

    fun toSelectionVO() = PlaylistSelectionVO(
        id = id,
        playlistName = playlistName
    )
}

data class Playlist(
    val id: Int,
    val playlistName: String,
    val playlistItems: List<Voice>,
)

data class PlaylistSelectionVO(
    val id: Int,
    val playlistName: String,
)
