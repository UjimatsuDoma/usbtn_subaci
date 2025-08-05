package prac.tanken.shigure.ui.subaci.data.model.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register

class ResourceDownloadPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register<ResourceDownloadTask>("downloadResource") {
                this.dir.set(projectDir)
            }
        }
    }

}