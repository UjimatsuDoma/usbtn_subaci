package prac.tanken.shigure.ui.subaci.feature.base.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable

@Serializable
open class MainDestination(
    @param:StringRes val displayName: Int,
    @param:StringRes val desc: Int,
    @param:DrawableRes val unselectedIcon: Int,
    @param:DrawableRes val selectedIcon: Int,
)