package prac.tanken.shigure.ui.subaci.plugin

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import prac.tanken.shigure.ui.subaci.core.common.nio.downloadFile
import prac.tanken.shigure.ui.subaci.core.common.serialization.encodeJsonString
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.model.category.Category
import prac.tanken.shigure.ui.subaci.core.data.model.plugin.BASE_URL
import prac.tanken.shigure.ui.subaci.core.data.model.plugin.categoryRegex
import prac.tanken.shigure.ui.subaci.core.data.model.plugin.sourceRegex
import prac.tanken.shigure.ui.subaci.core.data.model.plugin.voiceRegex
import prac.tanken.shigure.ui.subaci.core.data.model.source.Source
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import java.io.File
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URI

const val htmlUrl = "${BASE_URL}/usbtn.html"

fun getVoices(url: String): List<Voice> {
    val html = URI(url).toURL().readText()
    val matches = voiceRegex.findAll(html)
    val voices = matches.map { parseJsonString<Voice>(it.groupValues[0]) }.toList()
    return voices.toList()
}

fun getCategories(url: String): List<Category> {
    val html = URI(url).toURL().readText()
    val matches = categoryRegex.findAll(html)
    val categories = matches.map {
        val categoryJson = it.groupValues[0]
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

fun getSources(url: String): List<Source> {
    val html = URI(url).toURL().readText()
    val matches = sourceRegex.findAll(html)
    val sources = matches.map { Source(it.groupValues[1], it.groupValues[2]) }.toList()
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
        val con = URI(url).toURL().openConnection() as HttpURLConnection
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

abstract class ResourceDownloadTask : DefaultTask() {
    @get:InputDirectory
    abstract val destination: DirectoryProperty

    @TaskAction
    fun run() {
        val file = destination.asFile.get()

        // download resources
        var tries = 0
        while (tries < 10) {
            try {
                println("fetching voices json...")
                val voices = getVoices(htmlUrl)
                FileWriter("$file/src/main/res/raw/audio_list.json").use {
                    it.write(encodeJsonString(voices))
                }
                println("fetching voices files...")
                val voicePath = "$file/src/main/assets/subaciAudio/"
                voices.forEach { voice ->
                    voice.src.apply {
                        val relPath = substringAfter("/")
                        val fileName = substringAfterLast("/")
                        val downloadUrl = "${BASE_URL}/$relPath"
                        val downloadDest = voicePath + fileName
                        val downloadFile = File(voicePath, fileName)
                        if (!downloadFile.exists()) {
                            URI(downloadUrl).toURL().downloadFile(
                                downloadDest,
                                mapOf(
                                    "Referer" to "https://leiros.cloudfree.jp/usbtn/usbtn.html"
                                )
                            )
                        }
                    }
                }
                println("fetching categories json...")
                val categories = getCategories(htmlUrl)
                FileWriter("$file/src/main/res/raw/class_list.json").use {
                    it.write(encodeJsonString(categories))
                }
                println("fetching sources json...")
                val sources = getSources(htmlUrl)
                FileWriter("$file/src/main/res/raw/video_list.json").use {
                    it.write(encodeJsonString(sources))
                }
                println("fetching thumbnails...")
                val sourcesThumbnailPath = "$file/src/main/assets/subaciThumbs/"
                sources.forEach { source ->
                    val id = source.videoId
                    val fileName = "$id.jpg"
                    val downloadFile = File(sourcesThumbnailPath, fileName)
                    if (!downloadFile.exists()) {
                        val downloadUrl = getMaxResolutionThumbUrl(id)
                        URI(downloadUrl).toURL().downloadFile(
                            sourcesThumbnailPath + fileName,
                        )
                    }
                }
                break
            } catch (e: Exception) {
                e.printStackTrace()
                println("ERROR RETRY: ${tries + 1}/10")
                tries++
                Thread.sleep(2000)
                continue
            }
        }

        // copy downloaded resources
    }
}