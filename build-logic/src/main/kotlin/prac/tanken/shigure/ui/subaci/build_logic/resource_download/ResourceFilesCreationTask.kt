package prac.tanken.shigure.ui.subaci.build_logic.resource_download

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ResourceFilesCreationTask: DefaultTask() {
    infix fun File.createFileIfNotExists(fileName: String) {
        val childFile = File(this, fileName)
        if (!childFile.exists()) childFile.createNewFile()
    }

    infix fun File.createFolderIfNotExists(folderName: String) {
        val childFolder = File(this, folderName)
        if (!childFolder.exists()) childFolder.mkdirs()
    }

    @get:InputDirectory
    abstract val destination: DirectoryProperty

    @TaskAction
    fun run() {
        val file = destination.asFile.get()
        val rawResDir = File("$file/src/main/res/raw/")
        if(!rawResDir.exists()) rawResDir.mkdirs()
        rawResDir createFileIfNotExists "audio_list.json"
        rawResDir createFileIfNotExists "class_list.json"
        rawResDir createFileIfNotExists "video_list.json"

        val assetsDir = File("$file/src/main/assets/")
        if(!assetsDir.exists()) assetsDir.mkdirs()
    }
}