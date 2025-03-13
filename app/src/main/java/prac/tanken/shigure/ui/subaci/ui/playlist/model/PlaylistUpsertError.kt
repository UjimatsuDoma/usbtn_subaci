package prac.tanken.shigure.ui.subaci.ui.playlist.model

import kotlinx.serialization.Serializable

@Serializable
sealed class PlaylistUpsertError {
    @Serializable
    data object BlankName : PlaylistUpsertError()

    @Serializable
    data object ReplicatedName : PlaylistUpsertError()
}