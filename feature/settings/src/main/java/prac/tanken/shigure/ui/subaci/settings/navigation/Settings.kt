package prac.tanken.shigure.ui.subaci.settings.navigation

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.base.navigation.MainDestinations
import prac.tanken.shigure.ui.subaci.settings.R as TankenR
import com.microsoft.fluent.mobile.icons.R as FluentR

@Serializable
data object Settings : MainDestinations(
    displayName = TankenR.string.home_settings,
    desc = TankenR.string.home_settings_desc,
    unselectedIcon = FluentR.drawable.ic_fluent_settings_24_regular,
    selectedIcon = FluentR.drawable.ic_fluent_settings_24_filled,
)