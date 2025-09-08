package prac.tanken.shigure.ui.subaci.core.ui.settings

import kotlinx.serialization.Serializable

@Serializable
data class UiSettings(
    val appColor: AppColor = AppColor.default,
    val appDarkMode: AppDarkMode = AppDarkMode.default,
    val bottomBarLabelBehaviour: NavigationLabelBehaviour = NavigationLabelBehaviour.default
)
