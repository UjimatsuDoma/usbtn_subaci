package prac.tanken.shigure.ui.subaci.data.model.voices

import prac.tanken.shigure.ui.subaci.ui.voices.model.VoicesVO

typealias VoiceGroups = Map<String, List<VoicesVO>>
typealias VoiceGroup = Map.Entry<String, List<VoicesVO>>
fun mutableVoiceGroups() = mutableMapOf<String, List<VoicesVO>>()