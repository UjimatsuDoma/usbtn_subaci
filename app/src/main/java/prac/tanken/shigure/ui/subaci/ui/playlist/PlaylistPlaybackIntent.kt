package prac.tanken.shigure.ui.subaci.ui.playlist

sealed class PlaylistPlaybackIntent {
    data object Play : PlaylistPlaybackIntent()
    data object Stop : PlaylistPlaybackIntent()
}