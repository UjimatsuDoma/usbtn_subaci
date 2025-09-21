package prac.tanken.shigure.ui.subaci.core.data.settings.ui

import kotlinx.serialization.Serializable

@Serializable
data class UiSettings(
    val appColor: AppColor = AppColor.Companion.default,
    val appDarkMode: AppDarkMode = AppDarkMode.Companion.default,
    val bottomBarLabelBehaviour: NavigationLabelBehaviour = NavigationLabelBehaviour.Companion.default
)
