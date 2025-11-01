package prac.tanken.shigure.ui.subaci.feature.voices.model

import prac.tanken.shigure.ui.subaci.core.data.model.voices.Category

data class CategoryVO(
    val className: String,
    val sectionId: String,
)

fun Category.toCategoryVO() = CategoryVO(
    className = className,
    sectionId = sectionId
)