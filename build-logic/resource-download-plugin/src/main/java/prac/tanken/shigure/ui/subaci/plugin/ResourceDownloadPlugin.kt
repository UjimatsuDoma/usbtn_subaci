package prac.tanken.shigure.ui.subaci.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import java.io.File

class ResourceDownloadPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register<ResourceDownloadTask>("downloadResource") {
                destination.set(projectDir)
            }
        }
    }

}