package prac.tanken.shigure.ui.subaci.core.data.model.voices

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.model.Voice

@Serializable
data class VoiceReference(val id: String)

fun Voice.toReference() = VoiceReference(id)