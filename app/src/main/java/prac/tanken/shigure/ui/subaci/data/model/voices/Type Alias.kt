package prac.tanken.shigure.ui.subaci.data.model.voices

import prac.tanken.shigure.ui.subaci.data.model.Voice

typealias VoiceGroups = Map<String, List<Voice>>
typealias VoiceGroup = Map.Entry<String, List<Voice>>
fun mutableVoiceGroups() = mutableMapOf<String, List<Voice>>()