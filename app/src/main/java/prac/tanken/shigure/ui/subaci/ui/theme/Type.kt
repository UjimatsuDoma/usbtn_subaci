package prac.tanken.shigure.ui.subaci.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import prac.tanken.shigure.ui.subaci.R

val AppTypography = Typography()

val NotoSerifJPStatic = FontFamily(
    Font(R.font.noto_serif_jp_extra_light, FontWeight.ExtraLight),
    Font(R.font.noto_serif_jp_light, FontWeight.Light),
    Font(R.font.noto_serif_jp, FontWeight.Normal),
    Font(R.font.noto_serif_jp_medium, FontWeight.Medium),
    Font(R.font.noto_serif_jp_semi_bold, FontWeight.SemiBold),
    Font(R.font.noto_serif_jp_bold, FontWeight.Bold),
    Font(R.font.noto_serif_jp_extra_bold, FontWeight.ExtraBold),
    Font(R.font.noto_serif_jp_black, FontWeight.Black),
)

val NotoSerifJPVariable = FontFamily(
    Font(R.font.noto_serif_jp_variable)
)

//val NotoSerifJP =
//    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//        NotoSerifJPVariable
//    } else {
//        NotoSerifJPStatic
//    }

val NotoSerifJP = NotoSerifJPStatic

val NotoSansJPStatic = FontFamily(
    Font(R.font.noto_sans_jp_thin, FontWeight.Thin),
    Font(R.font.noto_sans_jp_extra_light, FontWeight.ExtraLight),
    Font(R.font.noto_sans_jp_light, FontWeight.Light),
    Font(R.font.noto_sans_jp, FontWeight.Normal),
    Font(R.font.noto_sans_jp_medium, FontWeight.Medium),
    Font(R.font.noto_sans_jp_semi_bold, FontWeight.SemiBold),
    Font(R.font.noto_sans_jp_bold, FontWeight.Bold),
    Font(R.font.noto_sans_jp_extra_bold, FontWeight.ExtraBold),
    Font(R.font.noto_sans_jp_black, FontWeight.Black),
)

val NotoSansJP = NotoSansJPStatic
