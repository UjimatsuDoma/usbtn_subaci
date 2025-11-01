package prac.tanken.shigure.ui.subaci.feature.settings.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.feature.base.navigation.MainDestination
import prac.tanken.shigure.ui.subaci.feature.settings.R as TankenR
import com.microsoft.fluent.mobile.icons.R as FluentR

@Serializable
data object Settings : MainDestination(
    displayName = TankenR.string.settings_nav_dest,
    desc = TankenR.string.settings_nav_dest_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_settings_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_settings_24_filled,
)