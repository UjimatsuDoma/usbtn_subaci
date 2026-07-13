package prac.tanken.shigure.ui.subaci.feature.voices.model

import prac.tanken.shigure.ui.subaci.core.data.model.Voice

typealias VoiceGroups = Map<String, List<Voice>>
typealias VoiceGroup = Map.Entry<String, List<Voice>>

fun mutableVoiceGroups() = mutableMapOf<String, List<Voice>>()