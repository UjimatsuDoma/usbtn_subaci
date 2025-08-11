package prac.tanken.shigure.ui.subaci.core.data.model.playlist

data class PlaylistSelectionVO(
    val id: Long,
    val playlistName: String,
)

fun Playlist.toSelectionVO() = PlaylistSelectionVO(
    id = id,
    playlistName = playlistName
)

fun PlaylistEntity.toSelectionVO() = PlaylistSelectionVO(
    id = id,
    playlistName = playlistName
)