package prac.tanken.shigure.ui.subaci.core.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Source(
    val videoId: String,
    val title: String,
)
