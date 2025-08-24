package prac.tanken.shigure.ui.subaci.base.model.voice

import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice

typealias VoiceGroups = Map<String, List<Voice>>
typealias VoiceGroup = Map.Entry<String, List<Voice>>
fun mutableVoiceGroups() = mutableMapOf<String, List<Voice>>()