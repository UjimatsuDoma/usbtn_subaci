import com.android.build.gradle.internal.tasks.factory.dependsOn
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.register
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.CategoriesDownloadTask
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.ResTempDirCreationTask
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.SourcesDownloadTask
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.VoicesDownloadTask

class ResourceDownloadPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            val vdt = tasks.register<VoicesDownloadTask>("downloadVoices")
            val cdt = tasks.register<CategoriesDownloadTask>("downloadCategories")
            val sdt = tasks.register<SourcesDownloadTask>("downloadSources")
            val rtdct = tasks.register<ResTempDirCreationTask>("checkIfTempDirExist")
            val log = tasks.register("logDownloadFin") {
                doLast {
                    logger.info("RESOURCE DOWNLOAD FINISHED")
                }
            }

            listOf(vdt, cdt, sdt).forEach {
                it.dependsOn(rtdct)
                log.dependsOn(it)
            }
        }
    }
}