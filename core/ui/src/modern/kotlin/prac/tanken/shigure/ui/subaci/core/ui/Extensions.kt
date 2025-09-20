package prac.tanken.shigure.ui.subaci.core.ui

import android.content.Context
import android.graphics.fonts.FontVariationAxis
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.unit.Density
import androidx.compose.ui.util.fastMap

fun FontVariation.Settings.toVariationSettings(
    context: Context?
): Array<FontVariationAxis> {
    val density = if (context != null) {
        Density(context)
    } else {
        // cannot reach
        throw IllegalStateException("Required context, but not provided")
    }
    return settings.fastMap { setting ->
        FontVariationAxis(setting.axisName, setting.toVariationValue(density))
    }.toTypedArray()
}