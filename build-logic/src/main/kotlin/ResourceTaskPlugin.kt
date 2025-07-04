import com.android.build.api.dsl.ApplicationExtension
import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.kotlin.dsl.configure
import org.gradle.api.Plugin
import org.gradle.api.Project
import util.BASE_URL
import util.downloadFileFromUrl
import util.encodeJsonString
import util.getCategories
import util.getMaxResolutionThumbUrl
import util.getSources
import util.getVoices
import java.io.File
import java.io.FileWriter

class ResourceTaskPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            extensions.configure<ApplicationExtension> {
                tasks.apply {
                    register("downloadResource") {
                        doLast {
                            downloadResource(target)
                        }
                    }
                    named("preBuild").dependsOn("downloadResource")
                }
            }
        }
    }

    private fun downloadResource(target: Project) {
        with(target){
            var tries = 0
            while (tries < 10) {
                try {
                    val htmlUrl = "$BASE_URL/usbtn.html"

                    val voices = getVoices(htmlUrl)
                    FileWriter("${projectDir}/src/main/res/raw/audio_list.json").use {
                        it.write(encodeJsonString(voices))
                    }
                    val voicePath = "${projectDir}/src/main/assets/subaciAudio/"
                    voices.forEach { voice ->
                        voice.src.apply {
                            val relPath = substringAfter("/")
                            val fileName = substringAfterLast("/")
                            val downloadUrl = "$BASE_URL/$relPath"
                            val downloadDest = voicePath + fileName
                            val downloadFile = File(voicePath, fileName)
                            if (!downloadFile.exists()) {
                                downloadFileFromUrl(
                                    downloadUrl,
                                    downloadDest,
                                    mapOf(
                                        "Referer" to "https://leiros.cloudfree.jp/usbtn/usbtn.html"
                                    )
                                )
                            }
                        }
                    }

                    val categories = getCategories(htmlUrl)
                    FileWriter("${projectDir}/src/main/res/raw/class_list.json").use {
                        it.write(encodeJsonString(categories))
                    }

                    val sources = getSources(htmlUrl)
                    FileWriter("${projectDir}/src/main/res/raw/video_list.json").use {
                        it.write(encodeJsonString(sources))
                    }
                    val sourcesThumbnailPath = "${projectDir}/src/main/assets/subaciThumbs/"
                    sources.forEach { source ->
                        val id = source.videoId
                        val fileName = "$id.jpg"
                        val downloadFile = File(sourcesThumbnailPath, fileName)
                        if (!downloadFile.exists()) {
                            val downloadUrl = getMaxResolutionThumbUrl(id)
                            downloadFileFromUrl(
                                downloadUrl,
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
        }
    }
}