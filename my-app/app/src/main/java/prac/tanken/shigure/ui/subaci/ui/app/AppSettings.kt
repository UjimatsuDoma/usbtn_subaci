package prac.tanken.shigure.ui.subaci.ui.app

import kotlinx.serialization.Serializable

@Serializable
data class AppSettings(
    val appColor: AppColor = AppColor.default,
    val appDarkMode: AppDarkMode = AppDarkMode.default,
    val bottomBarLabelBehaviour: BottomBarLabelBehaviour = BottomBarLabelBehaviour.default
)