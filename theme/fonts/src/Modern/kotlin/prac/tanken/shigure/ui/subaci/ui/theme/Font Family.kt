package prac.tanken.shigure.ui.subaci.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import prac.tanken.shigure.ui.subaci.getLocaleListByLocale
import prac.tanken.shigure.ui.subaci.ja
import prac.tanken.shigure.ui.subaci.matchLocale
import prac.tanken.shigure.ui.subaci.multiLang

val NotoSansMultiLang @Composable get() = getNotoFamily(SANS, multiLang)
val NotoSansJP @Composable get() = getNotoFamily(SANS, ja)
val NotoSerifMultiLang @Composable get() = getNotoFamily(SERIF, multiLang)
val NotoSerifJP @Composable get() = getNotoFamily(SERIF, ja)

val weights = listOf(
    FontWeight.Thin,
    FontWeight.ExtraLight,
    FontWeight.Light,
    FontWeight.Normal,
    FontWeight.Medium,
    FontWeight.SemiBold,
    FontWeight.Bold,
    FontWeight.ExtraBold,
    FontWeight.Black,
)

const val SANS = "sans"
const val SERIF = "serif"

const val normalSans = "noto_sans_variable.ttf"
const val italicSans = "noto_sans_variable_italic.ttf"
const val normalSerif = "noto_serif_variable.ttf"
const val italicSerif = "noto_serif_variable_italic.ttf"

@Composable
fun getNotoFamily(family: String, locale: Locale): FontFamily {
    val matchedLocale = matchLocale(locale)

    val normalId = when (family) {
        SANS -> normalSans
        SERIF -> normalSerif
        else -> {
            throw IllegalArgumentException("Wrong argument: font family")
        }
    }
    val italicId = when (family) {
        SANS -> italicSans
        SERIF -> italicSerif
        else -> {
            throw IllegalArgumentException("Wrong argument: font family")
        }
    }

    val list = mutableListOf<Font>()
    weights.forEach { wgt ->
        getLocaleListByLocale(matchedLocale).forEach { loc ->
            val normal = Font(
                path = "$loc/$normalId",
                assetManager = LocalContext.current.assets,
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(wgt.weight)
                ),
                weight = wgt,
            )
            val italic = Font(
                path = "$loc/$italicId",
                assetManager = LocalContext.current.assets,
                variationSettings = FontVariation.Settings(
                    FontVariation.weight(wgt.weight),
                    FontVariation.italic(1f)
                ),
                weight = wgt,
                style = FontStyle.Italic
            )
            list.add(normal)
            list.add(italic)
        }
    }
    return FontFamily(list.toList())
}

