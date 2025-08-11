package prac.tanken.shigure.ui.subaci.core.data.model.voices

val voicesGroupedByItems = VoicesGroupedBy::class.sealedSubclasses.map { it.objectInstance as VoicesGroupedBy }