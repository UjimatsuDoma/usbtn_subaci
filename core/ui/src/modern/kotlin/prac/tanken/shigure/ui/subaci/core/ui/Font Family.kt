@file:OptIn(ExperimentalTextApi::class)

package prac.tanken.shigure.ui.subaci.core.ui

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import prac.tanken.shigure.ui.subaci.core.ui.theme.getNotoTypography

@Composable
fun getNotoFamily(
    style: NotoStyle,
    locale: NotoCJKLocale? = null,
): FontFamily {
    val cxt = LocalContext.current
    return remember {
        FontFamily(
            (100..900 step 100).map { fontWeight ->
                SUBACIAssetsFont(
                    assetManager = cxt.assets,
                    path = "notocjk/${style.fileName}.ttc",
                    weight = FontWeight(fontWeight),
                    style = FontStyle.Normal,
                    ttcIndex = locale?.ttcIndex,
                    variationSettings = FontVariation.Settings(
                        FontVariation.weight(fontWeight)
                    )
                )
            }
        )
    }
}

val NotoSansJP @Composable get() = getNotoFamily(NotoStyle.SANS, NotoCJKLocale.JP)

val NotoSerifJP @Composable get() = getNotoFamily(NotoStyle.SERIF, NotoCJKLocale.JP)

val NotoSansAutoLang @Composable get() = getNotoFamily(NotoStyle.SANS)

val NotoSerifAutoLang @Composable get() = getNotoFamily(NotoStyle.SERIF)

@Composable
fun WithNotoCJKTypography(
    style: NotoStyle,
    locale: NotoCJKLocale? = null,
    content: @Composable () -> Unit
) {
    val fontFamily = when (style) {
        NotoStyle.SANS -> {
            when (locale) {
                NotoCJKLocale.JP -> NotoSansJP
                else -> NotoSansAutoLang
            }
        }

        NotoStyle.SERIF -> {
            when (locale) {
                NotoCJKLocale.JP -> NotoSerifJP
                else -> NotoSerifAutoLang
            }
        }
    }

    MaterialTheme(
        typography = getNotoTypography(fontFamily),
        content = {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current.copy(
                    fontFeatureSettings = "\"locl\" " + if (locale != null) "0" else "1"
                ),
                content = content
            )
        }
    )
}

@Composable
fun NotoSerifAuto(content: @Composable () -> Unit) =
    WithNotoCJKTypography(
        style = NotoStyle.SERIF,
        content = content
    )

@Composable
fun NotoSansAuto(content: @Composable () -> Unit) =
    WithNotoCJKTypography(
        style = NotoStyle.SANS,
        content = content
    )

@Composable
fun NotoSerifJP(content: @Composable () -> Unit) =
    WithNotoCJKTypography(
        style = NotoStyle.SERIF,
        locale = NotoCJKLocale.JP,
        content = content
    )

@Composable
fun NotoSansJP(content: @Composable () -> Unit) =
    WithNotoCJKTypography(
        style = NotoStyle.SANS,
        locale = NotoCJKLocale.JP,
        content = content
    )

@Composable
fun TextStyle.withLocalStyle() =
    copy(
        fontFamily = LocalTextStyle.current.fontFamily,
        fontFeatureSettings = LocalTextStyle.current.fontFeatureSettings
    )

@Composable
fun TextStyle.notoCjkStyle(
    style: NotoStyle,
    locale: NotoCJKLocale? = null,
): TextStyle {
    val fontFamily = when (style) {
        NotoStyle.SANS -> {
            when (locale) {
                NotoCJKLocale.JP -> NotoSansJP
                else -> NotoSansAutoLang
            }
        }

        NotoStyle.SERIF -> {
            when (locale) {
                NotoCJKLocale.JP -> NotoSerifJP
                else -> NotoSerifAutoLang
            }
        }
    }

    return this.copy(
        fontFamily = fontFamily,
        fontFeatureSettings = "\"locl\" " + if (locale != null) "0" else "1"
    )
}

@Composable
fun TextStyle.notoSerifJP() = notoCjkStyle(NotoStyle.SERIF, NotoCJKLocale.JP)

@Composable
fun TextStyle.notoSansJP() = notoCjkStyle(NotoStyle.SANS, NotoCJKLocale.JP)

@Composable
fun TextStyle.notoSerif() = notoCjkStyle(NotoStyle.SERIF)

@Composable
fun TextStyle.notoSans() = notoCjkStyle(NotoStyle.SERIF)