package prac.tanken.shigure.ui.subaci.feature.base.model.voices

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy

@Serializable
sealed class VoicesGrouped(
    val voicesGroupedBy: VoicesGroupedBy,
) {
    abstract val voiceGroups: VoiceGroups

    @Serializable
    data class ByCategory(override val voiceGroups: VoiceGroups) :
        VoicesGrouped(VoicesGroupedBy.Category)

    @Serializable
    data class ByKana(override val voiceGroups: VoiceGroups) :
        VoicesGrouped(VoicesGroupedBy.Kana)
}