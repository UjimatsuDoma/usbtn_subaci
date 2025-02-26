package prac.tanken.shigure.ui.subaci.ui.app

import android.os.Build
import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.R as TankenR

@Serializable
sealed class AppColor(@StringRes val displayName: Int) {
    @Serializable
    data object Dynamic : AppColor(TankenR.string.app_color_dynamic)

    @Serializable
    data object Static : AppColor(TankenR.string.app_color_built_in)

    companion object {
        val default =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Dynamic else Static
    }
}