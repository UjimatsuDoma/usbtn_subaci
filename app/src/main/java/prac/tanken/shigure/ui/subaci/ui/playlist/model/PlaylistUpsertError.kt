package prac.tanken.shigure.ui.subaci.ui.playlist.model

sealed class PlaylistUpsertError {
    data object BlankName : PlaylistUpsertError()
    data object ReplicatedName : PlaylistUpsertError()
}