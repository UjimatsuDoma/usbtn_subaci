package prac.tanken.shigure.ui.subaci.base.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
open class MainDestinations(
    @StringRes val displayName: Int,
    @StringRes val desc: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int,
)