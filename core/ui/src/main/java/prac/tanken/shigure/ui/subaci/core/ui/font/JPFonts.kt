package prac.tanken.shigure.ui.subaci.core.ui.font

import androidx.annotation.FontRes
import prac.tanken.shigure.ui.subaci.core.ui.R

enum class JPFonts(@FontRes val resId: Int) {
    IPA_EX_GOTHIC(R.font.jp_ipaexg),
    LOGO_TYPE_GOTHIC(R.font.jp_logotypeg),
    NIJIMI_MEICHO(R.font.jp_nijimi),
    NIKUMARU(R.font.jp_nikumaru),
    YASASHISA_ANTIQUE(R.font.jp_yasaantic),
    YASASHISA_GOTHIC(R.font.jp_yasagothb);

    companion object {
        val default = YASASHISA_GOTHIC
    }
}