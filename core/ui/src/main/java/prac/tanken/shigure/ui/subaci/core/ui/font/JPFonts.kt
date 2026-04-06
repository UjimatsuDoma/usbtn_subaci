package prac.tanken.shigure.ui.subaci.core.ui.font

import androidx.annotation.FontRes
import androidx.annotation.StringRes
import prac.tanken.shigure.ui.subaci.core.ui.R

enum class JPFonts(
    @FontRes val fontResId: Int,
    @StringRes val displayName: Int,
) {
    IPA_EX_GOTHIC(R.font.jp_ipaexg, R.string.jp_ipaexg_name),
    LOGO_TYPE_GOTHIC(R.font.jp_logotypeg, R.string.jp_logotypeg_name),
    NIJIMI_MEICHO(R.font.jp_nijimi, R.string.jp_nijimi_name),
    NIKUMARU(R.font.jp_nikumaru, R.string.jp_nikumaru_name),
    YASASHISA_ANTIQUE(R.font.jp_yasaantic, R.string.jp_yasaantic_name),
    YASASHISA_GOTHIC(R.font.jp_yasagothb, R.string.jp_yasagothb_name);

    companion object {
        val default = YASASHISA_GOTHIC
    }
}