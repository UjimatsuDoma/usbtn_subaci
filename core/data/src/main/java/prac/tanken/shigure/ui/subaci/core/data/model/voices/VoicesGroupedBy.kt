package prac.tanken.shigure.ui.subaci.core.data.model.voices

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.R

@Serializable
sealed class VoicesGroupedBy(
    @param:StringRes val displayName: Int
) {
    @Serializable
    data object Category : VoicesGroupedBy(R.string.voices_grouped_by_category)
    @Serializable
    data object Kana : VoicesGroupedBy(R.string.voices_grouped_by_kana)

    @Serializable
    data object None : VoicesGroupedBy(R.string.voices_grouped_by_none)

    data object Video: VoicesGroupedBy(R.string.voices_grouped_by_none)
}

val voicesGroupedByItems = listOf(
    VoicesGroupedBy.None,
    VoicesGroupedBy.Kana,
    VoicesGroupedBy.Category
)