package prac.tanken.shigure.ui.subaci.playlist.model

sealed class PlaylistPlaybackIntent {
    data object Play : PlaylistPlaybackIntent()
    data object Stop : PlaylistPlaybackIntent()
}