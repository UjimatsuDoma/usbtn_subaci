package prac.tanken.shigure.ui.subaci.core.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import prac.tanken.shigure.ui.subaci.core.common.serialization.encodeJsonString
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString

@Entity(
    tableName = "playlists",
    indices = [Index(value = ["playlist_name"], unique = true)]
)
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "playlist_name") val playlistName: String,
    @ColumnInfo(name = "playlist_items") val playlistItems: String,
) {
    fun convertToPlaylist() = Playlist(
        id = id,
        playlistName = playlistName,
        playlistItemIds = parseJsonString<List<String>>(playlistItems)
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
        onDelete = ForeignKey.CASCADE
    )]
)
data class PlaylistSelectedEntity(
    @ColumnInfo("selected_id") val selectedId: Long,
    // 这个主键存在的意义：用于一个逻辑，该逻辑保证表里面只有一个数据
    @PrimaryKey val position: Int = 1,
) {
    fun toPlaylistSelected() = PlaylistSelected(selectedId)
}

val playlistNotSelected = PlaylistSelected(0)

data class Playlist(
    val id: Long,
    val playlistName: String,
    val playlistItemIds: List<String>,
) {
    fun toEntity() =
        PlaylistEntity(id, playlistName, encodeJsonString(playlistItemIds))
}

data class PlaylistSelected(
    val selectedId: Long
)