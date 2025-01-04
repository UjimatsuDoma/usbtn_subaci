package prac.tanken.shigure.ui.subaci.data.model.voices

sealed class VoicesGrouped(
    val voicesGroupedBy: VoicesGroupedBy,
    open val voiceGroups: VoiceGroups,
) {
    data class ByCategory(override val voiceGroups: VoiceGroups) :
        VoicesGrouped(VoicesGroupedBy.Category, voiceGroups)

    data class ByKana(override val voiceGroups: VoiceGroups) :
        VoicesGrouped(VoicesGroupedBy.Kana, voiceGroups)
}