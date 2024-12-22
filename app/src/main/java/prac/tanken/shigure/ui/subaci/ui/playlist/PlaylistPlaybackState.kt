package prac.tanken.shigure.ui.subaci.ui.playlist

sealed class PlaylistPlaybackState {
    data class Playing(val index: Int, val looping: Boolean = false): PlaylistPlaybackState()
    data object Stopped: PlaylistPlaybackState()
}