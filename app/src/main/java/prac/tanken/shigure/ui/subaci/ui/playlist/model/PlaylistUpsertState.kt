package prac.tanken.shigure.ui.subaci.ui.playlist.model

import androidx.compose.runtime.Stable
import kotlinx.serialization.Serializable

@Serializable
sealed class PlaylistUpsertState {
    @Serializable
    data object Closed : PlaylistUpsertState()

    @Serializable
    @Stable
    data class Draft(
        val action: PlaylistUpsertIntent,
        val name: String,
        val errors: List<PlaylistUpsertError> = emptyList(),
    ) : PlaylistUpsertState()
}

