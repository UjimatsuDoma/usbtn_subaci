package prac.tanken.shigure.ui.subaci.core.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed class UiText {
    data class DynamicText(val value: String): UiText()
    class ResourceString(
        @StringRes val resId: Int,
        vararg val args: Any,
    ): UiText()
    class ConcatenatedResourceString(
        vararg val resourceString: ResourceString,
    ): UiText()

    @Composable
    fun asString(): String = when(this) {
        is DynamicText -> value
        is ResourceString -> stringResource(resId, *args)
        is ConcatenatedResourceString -> buildString {
            resourceString.forEach { string ->
                append(string.asString())
            }
        }
    }

    fun asString(context: Context): String = when(this) {
        is DynamicText -> value
        is ResourceString -> context.getString(resId, *args)
        is ConcatenatedResourceString -> buildString {
            resourceString.forEach { string ->
                val str = string.asString(context)
                append(str)
            }
        }
    }
}