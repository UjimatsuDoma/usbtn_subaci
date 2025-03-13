package prac.tanken.shigure.ui.subaci.ui.playlist.model

import kotlinx.serialization.Serializable

@Serializable
sealed class PlaylistUpsertIntent {
    @Serializable
    data object Insert : PlaylistUpsertIntent()

    @Serializable
    data class Update(val originalId: Long) : PlaylistUpsertIntent()
}