package prac.tanken.shigure.ui.subaci.model

import kotlinx.serialization.Serializable

@Serializable
data class Category (
    val className: String,
    val sectionId: String,
    val idList: List<VoiceReference>
)

@Serializable
data class VoiceReference(val id: String)