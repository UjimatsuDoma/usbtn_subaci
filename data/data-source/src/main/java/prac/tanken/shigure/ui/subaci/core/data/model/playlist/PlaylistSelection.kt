package prac.tanken.shigure.ui.subaci.core.data.model.playlist

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "playlist_selection",
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
data class PlaylistSelection(
    @ColumnInfo("selected_id") val selectedId: Long,
    // 这个主键存在的意义：用于一个逻辑，该逻辑保证表里面只有一个数据
    @PrimaryKey val position: Int = 1,
)