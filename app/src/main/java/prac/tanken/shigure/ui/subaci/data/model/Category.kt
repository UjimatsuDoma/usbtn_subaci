package prac.tanken.shigure.ui.subaci.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Category (
    val className: String,
    val sectionId: String,
    val idList: List<VoiceReference>
) {
    fun toCategoryDTO() = CategoryDTO(
        className = className,
        sectionId = sectionId
    )
}

data class CategoryDTO(
    val className: String,
    val sectionId: String,
)

@Serializable
data class VoiceReference(val id: String)