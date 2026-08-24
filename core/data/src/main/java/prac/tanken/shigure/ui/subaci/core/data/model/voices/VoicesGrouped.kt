package prac.tanken.shigure.ui.subaci.core.data.model.voices

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.sources.SourceEntity

@Serializable
sealed class VoicesGrouped<Key>(
    val voicesGroupedBy: VoicesGroupedBy,
) {
    abstract val voiceGroups: Map<Key, List<Voice>>

    @Serializable
    data class ByCategory(override val voiceGroups: Map<String, List<Voice>>) :
        VoicesGrouped<String>(VoicesGroupedBy.Category)

    @Serializable
    data class ByKana(override val voiceGroups: Map<String, List<Voice>>) :
        VoicesGrouped<String>(VoicesGroupedBy.Kana)

    @Serializable
    data class ByVideo(override val voiceGroups: Map<SourceEntity, List<Voice>>) :
        VoicesGrouped<SourceEntity>(VoicesGroupedBy.Video)

    // Default value in constructor for fallback
    @Serializable
    data class ByNone(override val voiceGroups: Map<Unit, List<Voice>>) :
        VoicesGrouped<Unit>(VoicesGroupedBy.None)
}