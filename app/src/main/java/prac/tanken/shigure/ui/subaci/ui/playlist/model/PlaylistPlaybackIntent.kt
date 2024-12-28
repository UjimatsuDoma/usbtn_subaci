package prac.tanken.shigure.ui.subaci.ui.playlist.model

sealed class PlaylistPlaybackIntent {
    data object Play : PlaylistPlaybackIntent()
    data object Stop : PlaylistPlaybackIntent()
}