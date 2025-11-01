package prac.tanken.shigure.ui.subaci.feature.base.model.voices

import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference

fun Voice.toVoicesVO() = VoicesVO(id, label, new ?: false, videoId)
fun VoicesVO.toReference() = VoiceReference(id)