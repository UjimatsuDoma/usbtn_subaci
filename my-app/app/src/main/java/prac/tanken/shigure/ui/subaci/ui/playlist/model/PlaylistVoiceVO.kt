package prac.tanken.shigure.ui.subaci.ui.playlist.model

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.data.model.Voice

@Serializable
data class PlaylistVoiceVO(
    val id: String,
    val label: String,
)

fun Voice.toPlaylistVoiceVO() = PlaylistVoiceVO(
    id = id,
    label = label,
)
