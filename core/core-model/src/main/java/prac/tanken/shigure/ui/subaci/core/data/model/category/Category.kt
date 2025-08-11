package prac.tanken.shigure.ui.subaci.core.data.model.category

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference

@Serializable
data class Category (
    val className: String,
    val sectionId: String,
    val idList: List<VoiceReference>
)