package prac.tanken.shigure.ui.subaci.feature.sources.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.feature.base.navigation.MainDestination
import com.microsoft.fluent.mobile.icons.R as FluentR
import prac.tanken.shigure.ui.subaci.feature.sources.R as TankenR

@Serializable
data object Sources : MainDestination(
    displayName = TankenR.string.home_sources,
    desc = TankenR.string.home_sources_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_video_clip_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_video_clip_24_filled,
)