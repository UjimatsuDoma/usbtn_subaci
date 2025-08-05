package prac.tanken.shigure.ui.subaci.ui.app

import android.os.Build
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.R as TankenR

@Serializable
sealed class AppDarkMode(@StringRes val displayName: Int) {
    @Serializable
    data object Dynamic : AppDarkMode(TankenR.string.app_dark_mode_dynamic)

    @Serializable
    data class Static(val isDark: Boolean) : AppDarkMode(
        if (isDark) TankenR.string.app_dark_mode_dark else TankenR.string.app_dark_mode_light
    )

    companion object {
        val default =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Dynamic else Static(true)
    }
}