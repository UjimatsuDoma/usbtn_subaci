package prac.tanken.shigure.ui.subaci.core.ui.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class LazyListItemKey(
    val index: Int,
    val itemJson: String,
): Parcelable