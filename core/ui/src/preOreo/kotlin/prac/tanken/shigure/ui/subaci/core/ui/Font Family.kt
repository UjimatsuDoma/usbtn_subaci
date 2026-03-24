package prac.tanken.shigure.ui.subaci.core.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoCJKLocale
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.font.fontsDir
import java.io.File

@Composable
fun getNotoFamilySingleLocale(
    style: NotoStyle,
    locale: NotoCJKLocale,
): FontFamily {
    val cxt = LocalContext.current
    val fontsDir = File(cxt.filesDir, "fonts")
    return remember {
        FontFamily(
            (1..9).map {
                val weight = it * 100
                Font(
                    file = File(fontsDir, "${style.fileName}_${locale.code}_$weight.ttf")
                )
            }
        )
    }
}

@Composable
fun getNotoFamilyByLocales(
    style: NotoStyle,
    locales: List<NotoCJKLocale>,
): FontFamily {
    val fontsDir = fontsDir
    return remember(style, locales) {
        FontFamily(
            (1..9).flatMap {
                val wght = it * 100
                locales.map { locale ->
                    Font(
                        file = File(fontsDir, "${style.fileName}_${locale.code}_$wght.ttf"),
                        weight = FontWeight(wght),
                        style = FontStyle.Normal
                    )
                }
            }
        )
    }
}

fun getNotoFamilyByLocalesNonComposable(
    context: Context,
    style: NotoStyle,
    locales: List<NotoCJKLocale>,
): FontFamily {
    val fontsDir = File(context.applicationContext.filesDir, "fonts")
    return FontFamily(
        (1..9).flatMap {
            val wght = it * 100
            locales.map { locale ->
                Font(
                    file = File(fontsDir, "${style.fileName}_${locale.code}_$wght.ttf"),
                    weight = FontWeight(wght),
                    style = FontStyle.Normal
                )
            }
        }
    )
}

val NotoSansJP @Composable get() = getNotoFamilySingleLocale(NotoStyle.SANS, NotoCJKLocale.JP)

val NotoSerifJP @Composable get() = getNotoFamilySingleLocale(NotoStyle.SERIF, NotoCJKLocale.JP)