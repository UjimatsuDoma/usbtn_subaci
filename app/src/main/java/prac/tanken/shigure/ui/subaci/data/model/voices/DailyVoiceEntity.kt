package prac.tanken.shigure.ui.subaci.data.model.voices

import kotlinx.serialization.Serializable

@Serializable
data class DailyVoiceEntity(
    val voiceId: String,
    val addDate: String
)
