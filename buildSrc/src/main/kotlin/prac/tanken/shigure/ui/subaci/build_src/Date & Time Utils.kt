package prac.tanken.shigure.ui.subaci.build_src

import java.time.ZoneId
import java.time.ZonedDateTime

val now = buildString {
    ZonedDateTime.now(ZoneId.systemDefault()).apply {
        append(String.format("%d%02d%02d", year, monthValue, dayOfMonth))
        append("-")
        append(String.format("%02d%02d%02d", hour, minute, second))
    }
}