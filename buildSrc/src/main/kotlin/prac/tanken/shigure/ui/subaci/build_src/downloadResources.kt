package prac.tanken.shigure.ui.subaci.build_src

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import prac.tanken.shigure.ui.subaci.build_src.model.Voice
import java.io.FileReader
import java.util.Scanner
import java.util.regex.Pattern
import org.apache.commons.io.FileUtils
import org.gradle.internal.declarativedsl.parsing.parse
import prac.tanken.shigure.ui.subaci.build_src.model.Category
import prac.tanken.shigure.ui.subaci.build_src.model.SourceEntity
import java.io.File
import java.net.URI

const val BASE_URL = "https://leiros.cloudfree.jp/usbtn"

val voiceRegex = buildString {
    append("\\{")
    append("\"id\":\".+\",")
    append("\\s*\"src\":\".+\",")
    append("\\s*\"volume\":\\d+\\.\\d+,")
    append("\\s*\"a\":\".+\",")
    append("\\s*\"k\":\".+\",")
    append("\\s*\"label\":\".+\"")
    append("(,\\s*\"new\":((true)|(false)))?")
    append("(,\\s*\"videoId\":\".+\")?")
    append("(,\\s*\"time\":\".+\")?")
    append("}")
}.toRegex()
val categoryRegex = buildString {
    append("\\{\\n")
    append("\\t*className:\".+\",\\n")
    append("\\t*sectionId:\".+\",\\n")
    append("\\t*idList:\\[\\n")
    append("(\\s*,?\\{\"id\":\".+\",?\\s*}.*?\\n)+")
    append("\\t*\\]\\n")
    append("\\t*\\}")
}.toRegex()
val sourceRegex = buildString {
    append("\\s+")
    append("<div>")
    append("<a href=\"https://www.youtube.com/watch\\?v=(.*)\" target=\"_blank\" class=\"ellipsis\">")
    append("(.*)")
    append("</a>")
    append("</div>")
    append("\\R")
}.toRegex()

fun readTextFile(path: String) = buildString {
    Scanner(FileReader(path)).useDelimiter("\\A").use { scanner ->
        while (scanner.hasNextLine()) {
            appendLine(scanner.nextLine())
        }
    }
}

inline fun <reified T> parseJsonString(jsonString: String): T {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString<T>(jsonString)
}

inline fun <reified T> encodeJsonString(entity: T): String {
    val json = Json { ignoreUnknownKeys = true }
    return json.encodeToString(entity)
}

fun url(url: String) = URI(url).toURL()

fun getVoices(htmlPath: String): List<Voice> {
    val html = readTextFile(htmlPath)
    val matches = voiceRegex.findAll(html)
    val voices = matches.map { parseJsonString<Voice>(it.groupValues[0]) }.toList()
    return voices.toList()
}

fun getCategories(htmlPath: String): List<Category> {
    val html = readTextFile(htmlPath)
    val matches = categoryRegex.findAll(html)
    val categories = matches.map {
        var categoryJson = it.groupValues[0]
            .replace("className", "\"className\"")
            .replace("sectionId", "\"sectionId\"")
            .replace("idList", "\"idList\"")
            .replace("//.*\\n".toRegex(), "\n")
            .replace(",\\s*}".toRegex(), "}")
        parseJsonString<Category>(categoryJson)
    }.toList()
    return categories
}

fun getSources(htmlPath: String): List<SourceEntity> {
    val html = readTextFile(htmlPath)
    val matches = sourceRegex.findAll(html)
    val sources = matches.map { SourceEntity(it.groupValues[1], it.groupValues[2]) }.toList()
    return sources
}