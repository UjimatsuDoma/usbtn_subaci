package prac.tanken.shigure.ui.subaci.playlist.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.base.navigation.MainDestinations
import prac.tanken.shigure.ui.subaci.playlist.R as PlaylistR
import com.microsoft.fluent.mobile.icons.R as FluentR

@Serializable
data object Playlist : MainDestinations(
    displayName = PlaylistR.string.playlist_nav_dest,
    desc = PlaylistR.string.playlist_nav_dest_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_filled,
)