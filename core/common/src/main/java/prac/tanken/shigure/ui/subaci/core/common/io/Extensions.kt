package prac.tanken.shigure.ui.subaci.core.common.io

import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import kotlin.sequences.forEach

fun InputStream.readText() = buildString {
    BufferedReader(InputStreamReader(this@readText)).use {
        it.lineSequence().forEach { line -> appendLine(line) }
    }
}