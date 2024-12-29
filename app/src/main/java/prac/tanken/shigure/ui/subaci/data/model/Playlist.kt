package prac.tanken.shigure.ui.subaci.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString

@Entity(
    tableName = "playlists",
    indices = [Index(value = ["playlist_name"], unique = true)]
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "playlist_name") val playlistName: String,
    @ColumnInfo(name = "playlist_items") val playlistItems: String,
) {
    fun toPlaylist(voices: List<Voice>) = Playlist(
        id = id,
        playlistName = playlistName,
        playlistItems = parseJsonString<List<String>>(playlistItems).map { voiceId->
            voices.filter { it.id==voiceId }.toList()[0]
        }.toList()
    )

    fun toSelectionVO() = PlaylistSelectionVO(
        id = id,
        playlistName = playlistName
    )
}

@Entity(
    tableName = "playlist_selected",
    indices = [Index(
        value = ["selected_id"],
        unique = true
    )],
    foreignKeys = [ForeignKey(
        entity = PlaylistEntity::class,
        parentColumns = ["id"],
        childColumns = ["selected_id"],
        onDelete = ForeignKey.Companion.CASCADE
    )]
)
data class PlaylistSelected(
    @ColumnInfo("selected_id") val selectedId: Long,
    @PrimaryKey val position: Int = 1,
)

val playlistNotSelected = PlaylistSelected(0)

data class Playlist(
    val id: Long,
    val playlistName: String,
    val playlistItems: List<Voice>,
) {
    fun toSelectionVO() = PlaylistSelectionVO(
        id = id,
        playlistName = playlistName
    )
}

data class PlaylistSelectionVO(
    val id: Long,
    val playlistName: String,
)
