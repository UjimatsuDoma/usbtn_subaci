package prac.tanken.shigure.ui.subaci.core.common.serialization

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

inline fun <reified T> parseJsonString(jsonString: String): T {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<T>(jsonString)
}

inline fun <reified T> encodeJsonString(entity: T): String {
    val json = Json { ignoreUnknownKeys = true }
    return json.encodeToString(entity)
}