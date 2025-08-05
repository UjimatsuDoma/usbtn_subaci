package prac.tanken.shigure.ui.subaci.data.model.category

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.data.model.voice.VoiceReference

@Serializable
data class Category (
    val className: String,
    val sectionId: String,
    val idList: List<VoiceReference>
)