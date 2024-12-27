package prac.tanken.shigure.ui.subaci.data.helper

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

object DailyVoiceHelper {
    val todayStr = {
        fun encode(date: LocalDate) = LocalDate.Formats.ISO_BASIC.format(date)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        encode(today)
    }.invoke()
}