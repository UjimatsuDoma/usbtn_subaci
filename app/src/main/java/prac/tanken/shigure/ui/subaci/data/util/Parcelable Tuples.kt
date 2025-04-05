@file:OptIn(Experimental::class)

package prac.tanken.shigure.ui.subaci.data.util

import android.os.Parcelable
import kotlinx.parcelize.Experimental
import kotlinx.parcelize.Parcelize

@Parcelize
data class LazyListItemKey(
    val index: Int,
    val itemJson: String,
): Parcelable

inline infix fun <reified T> Int.combineKey(item: T) = LazyListItemKey(this, encodeJsonString(item))
