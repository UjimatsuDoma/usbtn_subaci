package prac.tanken.shigure.ui.subaci.base.model.voice

import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference

fun Voice.toVoicesVO() = VoicesVO(id, label, new ?: false, videoId)
fun VoicesVO.toReference() = VoiceReference(id)