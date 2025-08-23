package prac.tanken.shigure.ui.subaci.playlist.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface PlaylistUpsertError {
    @Serializable
    data object BlankName : PlaylistUpsertError

    @Serializable
    data object ReplicatedName : PlaylistUpsertError

    @Serializable
    data object NameNotChanged : PlaylistUpsertError
}