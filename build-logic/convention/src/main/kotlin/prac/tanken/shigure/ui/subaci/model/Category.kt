package prac.tanken.shigure.ui.subaci.model

import kotlinx.serialization.Serializable

@Serializable
data class Category (
    val className: String,
    val sectionId: String,
    val idList: List<VoiceReference>
)

data class CategoryVO(
    val className: String,
    val sectionId: String,
)

fun Category.toCategoryVO() = CategoryVO(
    className = className,
    sectionId = sectionId
)