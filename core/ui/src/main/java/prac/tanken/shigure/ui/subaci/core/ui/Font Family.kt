@file:OptIn(ExperimentalTextApi::class)

package prac.tanken.shigure.ui.subaci.core.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoCJKLocale
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.font.fontsDir
import java.io.File

fun getNotoFamilyByLocalesNonComposable(
    context: Context,
    style: NotoStyle,
): FontFamily {
    val fontsDir = File(context.applicationContext.filesDir, "fonts")
    return FontFamily(
        (1..9).map {
            val wght = it * 100
            Font(
                file = File(fontsDir, "${style.fileName}.ttc"),
                weight = FontWeight(wght),
                style = FontStyle.Normal,
                variationSettings = FontVariation.Settings(
                    weight = FontWeight(wght),
                    style = FontStyle.Normal
                )
            )
        }
    )
}