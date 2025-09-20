package prac.tanken.shigure.ui.subaci.core.ui

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import prac.tanken.shigure.ui.subaci.core.ui.theme.getNotoTypography
import java.util.Locale

@Composable
fun getNotoFamily(
    style: NotoStyle,
    locale: NotoCJKLocale,
): FontFamily {
    val cxt = LocalContext.current
    return remember {
        FontFamily(
            Font(
                assetManager = cxt.assets,
                path = "noto_fonts/${style.fileName}/${locale.code}/regular.ttf",
                weight = FontWeight.W400,
            ),
            Font(
                assetManager = cxt.assets,
                path = "noto_fonts/${style.fileName}/${locale.code}/_bold.ttf",
                weight = FontWeight.W700,
            ),
        )
    }
}

val NotoSansJP @Composable get() = getNotoFamily(NotoStyle.SANS, NotoCJKLocale.JP)

val NotoSerifJP @Composable get() = getNotoFamily(NotoStyle.SERIF, NotoCJKLocale.JP)

val NotoSansSC @Composable get() = getNotoFamily(NotoStyle.SANS, NotoCJKLocale.CN)

val NotoSerifSC @Composable get() = getNotoFamily(NotoStyle.SERIF, NotoCJKLocale.CN)

val NotoSans @Composable get() = getNotoFamily(NotoStyle.SANS, NotoCJKLocale.DEFAULT)

val NotoSerif @Composable get() = getNotoFamily(NotoStyle.SERIF, NotoCJKLocale.DEFAULT)

val NotoSansAutoLang: FontFamily
    @Composable get() {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocalConfiguration.current.locales[0]
        } else Locale.getDefault()
        return when {
            locale.language == "ja" -> NotoSansJP
            locale.language == "zh" && locale.country == "CN" -> NotoSansSC
            else -> NotoSans
        }
    }

val NotoSerifAutoLang: FontFamily
    @Composable get() {
        val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocalConfiguration.current.locales[0]
        } else Locale.getDefault()
        return when {
            locale.language == "ja" -> NotoSerifJP
            locale.language == "zh" && locale.country == "CN" -> NotoSerifSC
            else -> NotoSerif
        }
    }

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
                NotoCJKLocale.CN -> NotoSansSC
                else -> NotoSansAutoLang
            }
        }

        NotoStyle.SERIF -> {
            when (locale) {
                NotoCJKLocale.JP -> NotoSerifJP
                NotoCJKLocale.CN -> NotoSerifSC
                else -> NotoSerifAutoLang
            }
        }
    }

    MaterialTheme(
        typography = getNotoTypography(fontFamily),
        content = content
    )
}