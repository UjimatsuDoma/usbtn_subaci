package prac.tanken.shigure.ui.subaci.core.data.model.sources

import kotlinx.serialization.Serializable

@Serializable
data class SourceEntity(
    val videoId: String,
    val title: String,
)
