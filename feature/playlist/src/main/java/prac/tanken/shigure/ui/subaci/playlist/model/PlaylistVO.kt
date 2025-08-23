package prac.tanken.shigure.ui.subaci.playlist.model

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistVO(
    val id: Long = 0,
    val playlistName: String = "",
    val voices: List<PlaylistVoiceVO> = emptyList(),
)

val playlistNotSelectedVO = PlaylistVO()
