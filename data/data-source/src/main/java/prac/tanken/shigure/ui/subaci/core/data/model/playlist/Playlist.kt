package prac.tanken.shigure.ui.subaci.core.data.model.playlist

import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice

data class Playlist(
    val id: Long,
    val playlistName: String,
    val playlistItems: List<Voice>,
)