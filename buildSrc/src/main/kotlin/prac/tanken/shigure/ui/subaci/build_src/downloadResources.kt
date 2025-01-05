package prac.tanken.shigure.ui.subaci.build_src

import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import prac.tanken.shigure.ui.subaci.build_src.model.Voice
import java.io.FileReader
import java.util.Scanner
import java.util.regex.Pattern
import org.apache.commons.io.FileUtils
import prac.tanken.shigure.ui.subaci.build_src.model.Category
import java.io.File
import java.net.URI

const val BASE_URL = "https://leiros.cloudfree.jp/usbtn"

val voiceRegex = """
    \{
    "id":".+",
    \s*"src":".+",
    \s*"volume":\d+\.\d+,
    \s*"a":".+",
    \s*"k":".+",
    \s*"label":".+"
    (,\s*"new":((true)|(false)))?
    (,\s*"videoId":".+")?
    (,\s*"time":".+")?
    }
""".trimIndent().replace(Regex("(\n*)\n"), "$1")
val categoryRegex = buildString {
    append("\\{\\n")
    append("\\t*className:\".+\",\\n")
    append("\\t*sectionId:\".+\",\\n")
    append("\\t*idList:\\[\\n")
    append("(\\s*,?\\{\"id\":\".+\",?\\s*}.*?\\n)+")
    append("\\t*\\]\\n")
    append("\\t*\\}")
}

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

fun getVoices(htmlPath: String, voiceDownloadDestination: String): String {
    val html = readTextFile(htmlPath)
    val matcher = Pattern.compile(voiceRegex).matcher(html)

    val voices = mutableListOf<Voice>()

    while (matcher.find()) {
        val voiceJson = matcher.group()
        val voice: Voice = parseJsonString(voiceJson)

        val relPath = voice.src.let { src -> src.substring(src.indexOfFirst { it == '/' }) }
        val fileName = voice.src.let { src -> src.substring(src.indexOfLast { it == '/' }) }
        val downloadUrl = BASE_URL + relPath
        val downloadFile = File(voiceDownloadDestination + fileName)
        if(!downloadFile.exists()) {
            FileUtils.copyURLToFile(URI(downloadUrl).toURL(), downloadFile)
        }

        voices.add(voice)
    }

    val voicesJson = Json.encodeToString<List<Voice>>(voices as List<Voice>)
    return voicesJson
}

fun getCategories(htmlPath: String): String {
    val html = readTextFile(htmlPath)
    val matcher = Pattern.compile(categoryRegex).matcher(html)

    val categories = mutableListOf<Category>()

    while (matcher.find()) {
        var categoryJson = matcher.group()
        categoryJson = categoryJson.replace("className", "\"className\"")
        categoryJson = categoryJson.replace("sectionId", "\"sectionId\"")
        categoryJson = categoryJson.replace("idList", "\"idList\"")
        categoryJson = categoryJson.replace("//.*\\n".toRegex(), "\n")
        categoryJson = categoryJson.replace(",\\s*}".toRegex(), "}")
        val category: Category = parseJsonString(categoryJson)
        categories.add(category)
    }

    val categoriesJson = Json.encodeToString<List<Category>>(categories as List<Category>)
    return categoriesJson
}