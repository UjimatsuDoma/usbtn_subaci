package prac.tanken.shigure.ui.subaci

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.intl.LocaleList

val ja = Locale("ja-JP")
val zhCN_Hans = Locale("zh-Hans-CN")
val enUS = Locale("en-US")

val localeList = listOf(
    zhCN_Hans, ja, enUS
)

fun matchLocale(locale: Locale) = when (locale.language) {
    "ja" -> ja
    "zh" if(locale.region in arrayOf("CN", "SG", "MY") || locale.script == "Hans") -> zhCN_Hans
    else -> enUS
}

val multiLang: Locale
    @Composable get() = LocaleList.current.localeList[0]

fun getLocaleListByLocale(locale: Locale) =
    localeList.let { list ->
        var newList = localeList - locale
        newList = newList.toMutableList().apply { add(0, locale) }.toList()
        return@let newList
    }
