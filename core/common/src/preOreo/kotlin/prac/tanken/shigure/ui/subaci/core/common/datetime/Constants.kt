package prac.tanken.shigure.ui.subaci.core.common.datetime

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object SynchronizationLock

val todayStr: String
    get() {
        fun encode(date: Date)= synchronized(SynchronizationLock) {
            return@synchronized SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)
        }

        val today = Date()
        return encode(today)
    }