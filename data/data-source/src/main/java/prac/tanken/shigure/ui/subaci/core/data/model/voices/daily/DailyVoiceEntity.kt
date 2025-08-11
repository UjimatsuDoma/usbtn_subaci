package prac.tanken.shigure.ui.subaci.core.data.model.voices.daily

import kotlinx.serialization.Serializable

@Serializable
data class DailyVoiceEntity(
    val voiceId: String,
    val addDate: String
)
