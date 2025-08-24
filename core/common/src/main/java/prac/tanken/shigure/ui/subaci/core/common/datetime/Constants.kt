package prac.tanken.shigure.ui.subaci.core.common.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import prac.tanken.shigure.ui.subaci.core.common.multithread.SynchronizationLock
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val todayStrKotlinX: String
    get() {
        fun encode(date: LocalDate) = LocalDate.Formats.ISO_BASIC.format(date)
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        return encode(today)
    }

val todayStrLegacy: String
    get() {
        fun encode(date: Date) = synchronized(SynchronizationLock) {
            return@synchronized SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)
        }
        val today = Date()
        return encode(today)
    }