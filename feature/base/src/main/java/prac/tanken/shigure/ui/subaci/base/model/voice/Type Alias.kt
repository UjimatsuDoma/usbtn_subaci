package prac.tanken.shigure.ui.subaci.base.model.voice

typealias VoiceGroups = Map<String, List<VoicesVO>>
typealias VoiceGroup = Map.Entry<String, List<VoicesVO>>
fun mutableVoiceGroups() = mutableMapOf<String, List<VoicesVO>>()