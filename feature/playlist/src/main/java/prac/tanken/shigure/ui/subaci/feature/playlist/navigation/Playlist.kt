package prac.tanken.shigure.ui.subaci.feature.playlist.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.feature.base.navigation.MainDestination
import com.microsoft.fluent.mobile.icons.R as FluentR
import prac.tanken.shigure.ui.subaci.feature.playlist.R as PlaylistR

@Serializable
data object Playlist : MainDestination(
    displayName = PlaylistR.string.playlist_nav_dest,
    desc = PlaylistR.string.playlist_nav_dest_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_filled,
)