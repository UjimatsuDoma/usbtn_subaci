package prac.tanken.shigure.ui.subaci.ui.playlist.model

sealed class PlaylistUpsertIntent {
    data object Insert : PlaylistUpsertIntent()
    data class Update(val originalId: Int) : PlaylistUpsertIntent()
}