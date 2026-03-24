package prac.tanken.shigure.ui.subaci.core.data.settings.ui

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.ui.font.JPFonts
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoCJKLocale
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoStyle

@Serializable
data class UiSettings(
    val appColor: AppColor = AppColor.default,
    val appDarkMode: AppDarkMode = AppDarkMode.default,
    val bottomBarLabelBehaviour: NavigationLabelBehaviour = NavigationLabelBehaviour.default,
    val cjkLocaleOrder: List<NotoCJKLocale> = NotoCJKLocale.entries.toList(),
    val notoStyle: NotoStyle = NotoStyle.SANS,
    val jpFont: JPFonts = JPFonts.YASASHISA_GOTHIC,
)
