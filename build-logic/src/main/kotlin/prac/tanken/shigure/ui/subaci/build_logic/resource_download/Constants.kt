package prac.tanken.shigure.ui.subaci.build_logic.resource_download

import org.gradle.api.Task
import org.gradle.api.tasks.TaskContainer

val Task.jsonDir get() = project.layout.buildDirectory.dir("subaciTmp/assets/subaciJson").get().asFile
val Task.voicesDir get() = project.layout.buildDirectory.dir("subaciTmp/assets/subaciAudio").get().asFile
val Task.thumbnailsDir get() = project.layout.buildDirectory.dir("subaciTmp/assets/subaciThumbs").get().asFile

val TaskContainer.downloadVoices get() = named("downloadVoices")
val TaskContainer.downloadCategories get() = named("downloadCategories")
val TaskContainer.downloadSources get() = named("downloadSources")
val TaskContainer.checkIfTempDirExist get() = named("checkIfTempDirExist")

/**
 * 「しぐれういボタン」网址主干域名
 */
const val BASE_URL = "https://leiros.cloudfree.jp/usbtn"
const val HTML_URL = "${BASE_URL}/usbtn.html"

/**
 * 以下正则表达式分别匹配语音数据、语音分类数据和语音来源数据。
 */
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