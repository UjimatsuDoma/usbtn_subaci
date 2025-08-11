package prac.tanken.shigure.ui.subaci.core.common.nio

import prac.tanken.shigure.ui.subaci.core.common.io.readText
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

fun URL.readText() = openStream().readText()

fun URL.downloadFile(
    dest: String,
    headerOptions: Map<String, String> = mapOf(),
) = try {
    // 创建HTTP连接
    val con = (openConnection() as HttpURLConnection).apply {
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