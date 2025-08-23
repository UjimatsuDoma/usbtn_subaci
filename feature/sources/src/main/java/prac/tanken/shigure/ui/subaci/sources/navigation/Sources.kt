package prac.tanken.shigure.ui.subaci.sources.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.base.navigation.MainDestinations
import prac.tanken.shigure.ui.subaci.sources.R as TankenR
import com.microsoft.fluent.mobile.icons.R as FluentR

@Serializable
data object Sources : MainDestinations(
    displayName = TankenR.string.home_sources,
    desc = TankenR.string.home_sources_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_video_clip_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_video_clip_24_filled,
)