package prac.tanken.shigure.ui.subaci.build_logic.resource_download

import kotlinx.serialization.Serializable
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import prac.tanken.shigure.ui.subaci.build_logic.common.downloadFile
import prac.tanken.shigure.ui.subaci.build_logic.common.encodeJsonString
import prac.tanken.shigure.ui.subaci.build_logic.common.parseJsonString
import prac.tanken.shigure.ui.subaci.build_logic.common.readToString
import java.io.File
import java.io.FileWriter
import java.net.HttpURLConnection
import java.net.URI

abstract class SourcesDownloadTask : DefaultTask() {
    @get:InputDirectory
    abstract val destination: DirectoryProperty

    fun getSources(): List<SourceEntity> {
        val html = URI(HTML_URL).toURL().readToString()
        val matches = sourceRegex.findAll(html)
        val sources = matches.map { SourceEntity(it.groupValues[1], it.groupValues[2]) }.toList()
        return sources
    }

    @TaskAction
    fun run() {
        val file = destination.asFile.get()

        repeat(10) { currentTry ->
            try {
                println("fetching sources json...")
                val sources = getSources()
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
                return@repeat
            } catch (e: Exception) {
                e.printStackTrace()
                logger.error("ERROR RETRY: ${currentTry + 1}/10")
                Thread.sleep(2000)
            }
        }
    }

    @Serializable
    data class SourceEntity(
        val videoId: String,
        val title: String,
    )

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
}