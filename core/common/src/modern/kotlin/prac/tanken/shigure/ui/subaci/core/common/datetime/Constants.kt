package prac.tanken.shigure.ui.subaci.core.common.datetime

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
val todayStr: String
    get() {
        fun encode(date: LocalDate) = LocalDate.Formats.ISO_BASIC.format(date)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return encode(today)
    }