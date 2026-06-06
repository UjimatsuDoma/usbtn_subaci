package prac.tanken.shigure.ui.subaci.build_logic.resource_download

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class ResTempDirCreationTask : DefaultTask() {
    fun File.createFolderIfNotExists() {
        if (!isDirectory) delete()
        if (!exists()) mkdirs()
    }

    @TaskAction
    fun run() {
        jsonDir.createFolderIfNotExists()
        voicesDir.createFolderIfNotExists()
        thumbnailsDir.createFolderIfNotExists()
    }
}