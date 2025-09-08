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
import java.net.URI

abstract class VoicesDownloadTask : DefaultTask() {
    @get:InputDirectory
    abstract val destination: DirectoryProperty

    fun getVoices(): List<Voice> {
        val html = URI(HTML_URL).toURL().readToString()
        val matches = voiceRegex.findAll(html)
        val voices = matches.map { parseJsonString<Voice>(it.groupValues[0]) }.toList()
        return voices.toList()
    }

    @TaskAction
    fun run() {
        val file = destination.asFile.get()

        repeat(10) { currentTry ->
            try {
                logger.info("fetching voices json...")
                val voices = getVoices()
                FileWriter("$file/src/main/res/raw/audio_list.json").use {
                    it.write(encodeJsonString(voices))
                }
                logger.info("fetching voices files...")
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
                return@repeat
            } catch (e: Exception) {
                e.printStackTrace()
                logger.error("ERROR RETRY: ${currentTry + 1}/10")
                Thread.sleep(2000)
            }
        }
    }

    @Serializable
    data class Voice(
        // 音频文件的唯一识别码
        val id: String,
        // 音频文件在服务器的相对路径。本程序不会用到这个，大概。
        val src: String,
        // 音量
        val volume: Double,
        // 首假名的五十音图所在行
        val a: String,
        // 排序码，通常是语音标题提取、转换假名的结果
        val k: String,
        // 语音标题
        val label: String,
        // 是否为2023年12月8日后添加，可选值
        val new: Boolean? = null,
        // 语音来源YouTube直播的序列号，可选值
        val videoId: String? = null,
        // 语音在来源YouTube直播的时间坐标，可选值
        val time: String? = null,
    )
}