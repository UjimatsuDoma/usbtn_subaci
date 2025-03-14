package prac.tanken.shigure.ui.subaci.ui.voices.model

import prac.tanken.shigure.ui.subaci.data.model.Voice

data class VoicesVO(
    val id: String,
    val label: String,
    val new: Boolean? = null,
    val videoId: String? = null,
)

fun Voice.toVoicesVO() = VoicesVO(
    id = id,
    label = label,
    new = new,
    videoId = videoId,
)
