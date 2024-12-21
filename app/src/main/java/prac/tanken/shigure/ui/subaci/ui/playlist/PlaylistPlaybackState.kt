package prac.tanken.shigure.ui.subaci.ui.playlist

sealed class PlaylistPlaybackState {
    data class Playing(val index: Int): PlaylistPlaybackState()
    data object Stopped: PlaylistPlaybackState()
}