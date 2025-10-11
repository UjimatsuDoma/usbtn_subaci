package prac.tanken.shigure.ui.subaci.core.data.model.voices

import kotlinx.serialization.Serializable

@Serializable
data class Category (
    val className: String,
    val sectionId: String,
    val idList: List<VoiceReference>
)