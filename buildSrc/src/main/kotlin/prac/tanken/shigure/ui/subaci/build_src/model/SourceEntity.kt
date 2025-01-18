package prac.tanken.shigure.ui.subaci.build_src.model

import kotlinx.serialization.Serializable

@Serializable
data class SourceEntity(
    val videoId: String,
    val title: String,
)
