package prac.tanken.shigure.ui.subaci

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.FontFamily
import prac.tanken.shigure.ui.subaci.core.data.settings.ui.AppColor
import prac.tanken.shigure.ui.subaci.core.data.settings.ui.AppDarkMode
import prac.tanken.shigure.ui.subaci.core.ui.font.JPFonts
import prac.tanken.shigure.ui.subaci.core.ui.font.LocalJPFont
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.core.ui.theme.getTypographyByFontFamily

@Composable
fun ShigureUiButtonAppComposeImplementationTheme(
    appColor: AppColor,
    appDarkMode: AppDarkMode,
    fontFamily: FontFamily,
    jpFont: JPFonts,
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

    CompositionLocalProvider(
        LocalJPFont provides jpFont
    ) {
        ShigureUiButtonAppComposeImplementationTheme(
            darkTheme = appDarkModeReal,
            dynamicColor = appColorReal,
            typography = getTypographyByFontFamily(fontFamily),
            content = content
        )
    }
}