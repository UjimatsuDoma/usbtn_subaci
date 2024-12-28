package prac.tanken.shigure.ui.subaci.ui.playlist.model

sealed class PlaylistUpsertState {
    data object Closed : PlaylistUpsertState()
    data class Draft(
        val action: PlaylistUpsertIntent,
        val name: String,
        val errors: List<PlaylistUpsertError> = emptyList(),
    ): PlaylistUpsertState()
}

