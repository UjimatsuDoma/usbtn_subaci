package prac.tanken.shigure.ui.subaci.data.util

import kotlinx.serialization.json.Json
import java.io.InputStream

inline fun <reified T> parseJsonString(jsonString: String): T {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<T>(jsonString)
}

fun readIS2Text(inputStream: InputStream): String = buildString {
    inputStream.use {
        it.bufferedReader().readLines().forEach { line ->
            appendLine(line)
        }
    }
}