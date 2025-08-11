package prac.tanken.shigure.ui.subaci.ui.playlist.model

import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import kotlinx.serialization.Serializable

@Serializable
data class PlaylistVoiceVO(
    val id: String,
    val label: String,
)

fun Voice.toPlaylistVoiceVO() = PlaylistVoiceVO(
    id = id,
    label = label,
)
