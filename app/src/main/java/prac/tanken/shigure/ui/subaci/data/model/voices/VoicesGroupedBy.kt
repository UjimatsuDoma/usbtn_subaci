package prac.tanken.shigure.ui.subaci.data.model.voices

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.R

@Serializable
sealed class VoicesGroupedBy(
    @StringRes val displayName: Int
) {
    @Serializable
    data object Category : VoicesGroupedBy(R.string.voices_grouped_by_category)
    @Serializable
    data object Kana : VoicesGroupedBy(R.string.voices_grouped_by_kana)
}

val voicesGroupedByItems = VoicesGroupedBy::class.sealedSubclasses.map { it.objectInstance as VoicesGroupedBy }