package prac.tanken.shigure.ui.subaci.core.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import prac.tanken.shigure.ui.subaci.core.data.model.settings.ui.AppColor
import prac.tanken.shigure.ui.subaci.core.data.model.settings.ui.AppDarkMode
import prac.tanken.shigure.ui.subaci.core.ui.theme.AppTypography
import prac.tanken.shigure.ui.subaci.core.ui.theme.darkScheme
import prac.tanken.shigure.ui.subaci.core.ui.theme.lightScheme

@Composable
fun ShigureUiButtonAppComposeImplementationTheme(
    appColor: AppColor,
    appDarkMode: AppDarkMode,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val appColorReal = when (appColor) {
        AppColor.Dynamic if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) -> appColor
        AppColor.Static -> appColor
        else -> AppColor.default
    }
    val appDarkModeReal = when (appDarkMode) {
        AppDarkMode.Dynamic -> isSystemInDarkTheme()
        is AppDarkMode.Static -> appDarkMode.isDark
    }
    val darkSchemeReal =
        if (appColorReal == AppColor.Dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicDarkColorScheme(context)
        } else darkScheme
    val lightSchemeReal =
        if (appColorReal == AppColor.Dynamic && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            dynamicLightColorScheme(context)
        } else lightScheme

    val colorScheme = if (appDarkModeReal) darkSchemeReal else lightSchemeReal

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
    ) {
        CompositionLocalProvider(
            LocalTextStyle provides LocalTextStyle.current.copy(
                fontFamily = NotoSansMultiLang
            ),
            content = {
                val view = LocalView.current
                if (!view.isInEditMode) {
                    SideEffect {
                        // copied from https://stackoverflow.com/a/73273051/24700045
//                        (view.context as Activity).window.statusBarColor =
//                            colorScheme.primary.toArgb()
//                        ViewCompat.getWindowInsetsController(view)?.isAppearanceLightStatusBars =
//                            !appDarkModeReal

                        val window = (view.context as Activity).window
                        window.statusBarColor = colorScheme.primary.toArgb()
                        WindowCompat.getInsetsController(window, view)
                            .isAppearanceLightStatusBars = !appDarkModeReal
                    }
                }

                content()
            }
        )
    }
}