package prac.tanken.shigure.ui.subaci.data.helper

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DailyVoiceHelper {
    val todayStr = {
        fun encode(date: Date) = synchronized(this) {
            return@synchronized SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)
        }

        val today = Date()
        encode(today)
    }.invoke()
}
