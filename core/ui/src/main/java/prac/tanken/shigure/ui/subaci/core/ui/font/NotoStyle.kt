package prac.tanken.shigure.ui.subaci.core.ui.font

import androidx.annotation.StringRes
import prac.tanken.shigure.ui.subaci.core.ui.R

enum class NotoStyle(
    val fileName: String,
    @StringRes val displayName: Int,
) {
    SANS("sans", R.string.noto_sans_display_name),
    SERIF("serif", R.string.noto_serif_display_name),
}