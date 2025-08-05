package prac.tanken.shigure.ui.subaci.data.model.plugin

/**
 * 「しぐれういボタン」网址主干域名
 */
const val BASE_URL = "https://leiros.cloudfree.jp/usbtn"

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