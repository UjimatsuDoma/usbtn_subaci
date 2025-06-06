package prac.tanken.shigure.ui.subaci.build_src

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.build_src.model.Category
import prac.tanken.shigure.ui.subaci.build_src.model.SourceEntity
import prac.tanken.shigure.ui.subaci.build_src.model.Voice
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.FileReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.RuntimeException
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.util.Scanner

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

fun url(url: String): URL = URI(url).toURL()
fun url2IS(url: String): InputStream = url(url).openStream()

fun readTextFromUrl(url: String) = buildString {
    BufferedReader(InputStreamReader(url2IS(url))).use {
        it.lineSequence().forEach { appendLine(it) }
    }
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

inline fun <reified T> encodeJsonString(entity: T): String {
    val json = Json { ignoreUnknownKeys = true }
    return json.encodeToString(entity)
}

fun getVoices(url: String): List<Voice> {
    val html = readTextFromUrl(url)
    val matches = voiceRegex.findAll(html)
    val voices = matches.map { parseJsonString<Voice>(it.groupValues[0]) }.toList()
    return voices.toList()
}

fun getCategories(url: String): List<Category> {
    val html = readTextFromUrl(url)
    val matches = categoryRegex.findAll(html)
    val categories = matches.map {
        var categoryJson = it.groupValues[0]
            // add quotation mark
            .replace("className", "\"className\"")
            .replace("sectionId", "\"sectionId\"")
            .replace("idList", "\"idList\"")
            // remove comments
            .replace("//.*\\n".toRegex(), "\n")
            // remove trailing commas
            .replace(",\\s*}".toRegex(), "}")
        parseJsonString<Category>(categoryJson)
    }.toList()
    return categories
}

fun getSources(url: String): List<SourceEntity> {
    val html = readTextFromUrl(url)
    val matches = sourceRegex.findAll(html)
    val sources = matches.map { SourceEntity(it.groupValues[1], it.groupValues[2]) }.toList()
    return sources
}

fun getMaxResolutionThumbUrl(videoId: String): String {
    // high to low
    val resolutions = listOf(
        "maxresdefault",
        "hq720",
        "mqdefault",
    )
    var result = ""
    for (resolution in resolutions) {
        val url = "http://i3.ytimg.com/vi/$videoId/$resolution.jpg"
        val con = url(url).openConnection() as HttpURLConnection
        con.requestMethod = "GET"
        con.connect()
        if (con.responseCode == 200) {
            result = url
            con.disconnect()
            break
        }
    }
    return result
}

fun downloadFileFromUrl(
    url: String,
    dest: String,
    headerOptions: Map<String, String> = mapOf(),
) = try {
    // 创建HTTP连接
    val con = (URL(url).openConnection() as HttpURLConnection).apply {
        requestMethod = "GET"
        headerOptions.forEach { option ->
            setRequestProperty(option.key, option.value)
        }
    }
    con.connect()
    // 拿到文件输入流和文件大小，确保文件可下载
    val inputStream = con.inputStream
    val fileSize = con.contentLength
    if (fileSize <= 0) throw RuntimeException("Cannot get size")
    if (inputStream == null) throw RuntimeException("Stream is null")
    // 目的路径不存在则创建
    val path = dest.substringBeforeLast("/")
    val fileName = dest.substringAfterLast("/")
    val dir = File(path)
    if (!dir.exists()) dir.mkdir()
    // 文件输出流
    val fos = FileOutputStream(dest)
    val buffer = ByteArray(1024)
    var downloadFileSize = 0
    do {
        val numRead = inputStream.read(buffer)
        if (numRead == -1) break
        fos.write(buffer, 0, numRead)
        downloadFileSize += numRead
    } while (true)
    inputStream.close()
} catch (e: Exception) {
    e.printStackTrace()
}