import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.CategoriesDownloadTask
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.SourcesDownloadTask
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.VoicesDownloadTask

class ResourceDownloadPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            tasks.register<VoicesDownloadTask>("downloadVoices") {
                destination.set(projectDir)
            }
            tasks.register<CategoriesDownloadTask>("downloadCategories") {
                destination.set(projectDir)
            }
            tasks.register<SourcesDownloadTask>("downloadSources") {
                destination.set(projectDir)
            }
        }
    }
}