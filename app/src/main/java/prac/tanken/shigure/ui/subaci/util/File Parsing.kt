package prac.tanken.shigure.ui.subaci.util

import kotlinx.serialization.json.Json
import java.io.InputStream

fun readIStoText(inputStream: InputStream): String =
    buildString {
        inputStream.bufferedReader().readLines().forEach { line ->
            append(line)
        }
    }

inline fun <reified T> parseJsonText(jsonString: String): T {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<T>(jsonString)
}