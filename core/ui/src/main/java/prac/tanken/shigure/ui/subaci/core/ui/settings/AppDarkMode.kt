package prac.tanken.shigure.ui.subaci.core.ui.settings

import android.os.Build
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.ui.R

@Serializable
sealed class AppDarkMode(@param:StringRes val displayName: Int) {
    @Serializable
    data object Dynamic : AppDarkMode(R.string.app_dark_mode_dynamic)

    @Serializable
    data class Static(val isDark: Boolean) : AppDarkMode(
        if (isDark) R.string.app_dark_mode_dark else R.string.app_dark_mode_light
    )

    companion object {
        val default =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) Dynamic else Static(true)
    }
}