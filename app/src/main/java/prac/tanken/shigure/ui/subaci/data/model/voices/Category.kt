package prac.tanken.shigure.ui.subaci.data.model.voices

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.data.model.Voice

@Serializable
data class Category (
    val className: String,
    val sectionId: String,
    val idList: List<VoiceReference>
) {
    fun toCategoryVO() = CategoryVO(
        className = className,
        sectionId = sectionId
    )
}

data class CategoryVO(
    val className: String,
    val sectionId: String,
)

@Serializable
data class VoiceReference(val id: String)

fun Voice.toReference() = VoiceReference(id)