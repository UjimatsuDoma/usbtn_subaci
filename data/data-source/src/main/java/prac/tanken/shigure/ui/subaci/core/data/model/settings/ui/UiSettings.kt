package prac.tanken.shigure.ui.subaci.core.data.model.settings.ui

import kotlinx.serialization.Serializable

@Serializable
data class UiSettings(
    val appColor: AppColor = AppColor.default,
    val appDarkMode: AppDarkMode = AppDarkMode.default,
    val bottomBarLabelBehaviour: BottomBarLabelBehaviour = BottomBarLabelBehaviour.default
)