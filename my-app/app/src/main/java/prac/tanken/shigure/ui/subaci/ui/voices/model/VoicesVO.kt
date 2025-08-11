package prac.tanken.shigure.ui.subaci.ui.voices.model

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference

@Serializable
data class VoicesVO(
    val id: String,
    val label: String,
    val a: String,
    val k: String,
    val new: Boolean? = null,
    val videoId: String? = null,
) {
    fun toReference() = VoiceReference(id)
}

fun Voice.toVoicesVO() = VoicesVO(
    id = id,
    label = label,
    a = a,
    k = k,
    new = new,
    videoId = videoId,
)
