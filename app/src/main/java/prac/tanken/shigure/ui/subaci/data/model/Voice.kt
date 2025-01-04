package prac.tanken.shigure.ui.subaci.data.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference

@Serializable
@Immutable
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
) {
    fun toReference() = VoiceReference(id)
}