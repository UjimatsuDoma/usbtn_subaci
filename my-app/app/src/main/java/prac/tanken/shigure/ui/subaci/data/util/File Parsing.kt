package prac.tanken.shigure.ui.subaci.data.util

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.InputStream

inline fun <reified T> parseJsonString(jsonString: String): T {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<T>(jsonString)
}

inline fun <reified T> encodeJsonString(entity: T): String {
    val json = Json { ignoreUnknownKeys = true }
    return json.encodeToString(entity)
}

fun readIS2Text(inputStream: InputStream): String = buildString {
    inputStream.use {
        it.bufferedReader().readLines().forEach { line ->
            appendLine(line)
        }
    }
}