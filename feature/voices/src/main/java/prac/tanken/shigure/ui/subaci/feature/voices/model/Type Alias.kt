package prac.tanken.shigure.ui.subaci.feature.voices.model

import prac.tanken.shigure.ui.subaci.feature.base.model.voices.VoicesVO

typealias VoiceGroups = Map<String, List<VoicesVO>>
typealias VoiceGroup = Map.Entry<String, List<VoicesVO>>

fun mutableVoiceGroups() = mutableMapOf<String, List<VoicesVO>>()