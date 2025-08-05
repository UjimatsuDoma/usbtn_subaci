package prac.tanken.shigure.ui.subaci

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.toLocalDateTime

fun main(){
    val now = Clock.System.now()
    val date = now.toLocalDateTime(TimeZone.currentSystemDefault()).date
    println(date.format(LocalDate.Formats.ISO_BASIC))
}