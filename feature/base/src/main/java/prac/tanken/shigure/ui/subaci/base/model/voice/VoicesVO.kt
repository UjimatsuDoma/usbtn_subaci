package prac.tanken.shigure.ui.subaci.base.model.voice

import kotlinx.serialization.Serializable

@Serializable
data class VoicesVO(
    val id: String,
    val label: String,
    val new: Boolean = false,
    val videoId: String? = null,
)
