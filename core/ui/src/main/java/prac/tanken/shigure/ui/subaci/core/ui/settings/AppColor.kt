package prac.tanken.shigure.ui.subaci.core.ui.settings

import android.os.Build
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.ui.R

@Serializable
sealed class AppColor(@param:StringRes val displayName: Int) {
    @Serializable
    data object Dynamic : AppColor(R.string.app_color_dynamic)

    @Serializable
    data object Static : AppColor(R.string.app_color_built_in)

    companion object {
        val default =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Dynamic else Static
    }
}