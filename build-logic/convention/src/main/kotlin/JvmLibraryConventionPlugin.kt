import com.android.build.gradle.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureCommonDependencies
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureKotlinJvm
import prac.tanken.shigure.ui.subaci.build_logic.convention.libs

class JvmLibraryConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("java").get().get().pluginId)
            apply(plugin = libs.findPlugin("jetbrains-kotlin-jvm").get().get().pluginId)

            group = "prac.tanken.shigure.ui.subaci"
            version = "unspecified"

            configureKotlinJvm()
            configureCommonDependencies()
        }
    }
}