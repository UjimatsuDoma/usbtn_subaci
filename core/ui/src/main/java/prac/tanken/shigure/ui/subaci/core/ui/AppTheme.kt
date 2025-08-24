package prac.tanken.shigure.ui.subaci.core.ui

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import prac.tanken.shigure.ui.subaci.core.data.model.settings.ui.AppColor
import prac.tanken.shigure.ui.subaci.core.data.model.settings.ui.AppDarkMode
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.core.ui.theme.getNotoTypography

@Composable
fun ShigureUiButtonAppComposeImplementationTheme(
    appColor: AppColor,
    appDarkMode: AppDarkMode,
    content: @Composable () -> Unit
) {
    val appColorReal = when (appColor) {
        AppColor.Dynamic if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> true
        else -> false
    }
    val appDarkModeReal = when (appDarkMode) {
        AppDarkMode.Dynamic -> isSystemInDarkTheme()
        is AppDarkMode.Static -> appDarkMode.isDark
    }

    ShigureUiButtonAppComposeImplementationTheme(
        darkTheme = appDarkModeReal,
        dynamicColor = appColorReal,
        typography = getNotoTypography(NotoSansAutoLang),
        content = content
    )
}