package prac.tanken.shigure.ui.subaci.core.data.model.playlist

import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice

fun PlaylistEntity.toPlaylist(voices: List<Voice>) =
    Playlist(
        id = id,
        playlistName = playlistName,
        playlistItems = parseJsonString<List<String>>(playlistItems).map { voiceId ->
            voices.filter { it.id == voiceId }.toList()[0]
        }.toList()
    )