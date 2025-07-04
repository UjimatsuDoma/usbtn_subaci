package model

import kotlinx.serialization.Serializable

@Serializable
data class SourceVideo(
    val id: String,
    val videoName: String,
)
