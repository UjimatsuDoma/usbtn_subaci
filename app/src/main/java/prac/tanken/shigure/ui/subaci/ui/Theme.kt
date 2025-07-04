package prac.tanken.shigure.ui.subaci.ui

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import prac.tanken.shigure.ui.subaci.ui.app.AppColor
import prac.tanken.shigure.ui.subaci.ui.app.AppDarkMode
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.ui.theme.darkScheme
import prac.tanken.shigure.ui.subaci.ui.theme.lightScheme

@Composable
fun ShigureUiButtonAppComposeImplementationTheme(
    appColor: AppColor,
    appDarkMode: AppDarkMode,
    content: @Composable() () -> Unit
) {
    val darkTheme = when (appDarkMode) {
        AppDarkMode.Dynamic -> isSystemInDarkTheme()
        is AppDarkMode.Static -> appDarkMode.isDark
    }

    val dynamicColor = when (appColor) {
        AppColor.Dynamic -> true
        AppColor.Static -> false
    }

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }

    ShigureUiButtonAppComposeImplementationTheme(
        darkTheme = darkTheme, dynamicColor = dynamicColor
    ) {
        val view = LocalView.current
        if (!view.isInEditMode) {
            SideEffect {
                // reference: https://stackoverflow.com/a/73273051/24700045

                val window = (view.context as Activity).window
                window.statusBarColor = colorScheme.primary.toArgb()
                WindowCompat.getInsetsController(window, view)
                    .isAppearanceLightStatusBars = !darkTheme
            }
        }

        content()
    }
}