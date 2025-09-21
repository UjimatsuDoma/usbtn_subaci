package prac.tanken.shigure.ui.subaci.feature.base.model.voices

import kotlinx.serialization.Serializable

@Serializable
data class VoicesVO(
    val id: String,
    val label: String,
    val new: Boolean = false,
    val videoId: String? = null,
)