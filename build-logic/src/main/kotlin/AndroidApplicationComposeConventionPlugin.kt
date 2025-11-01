import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureAndroidCompose
import prac.tanken.shigure.ui.subaci.build_logic.convention.libs

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("android-application").get().get().pluginId)
            apply(plugin = libs.findPlugin("kotlin-compose").get().get().pluginId)

            extensions.configure<ApplicationExtension> {
                configureAndroidCompose(this)
            }
        }
    }

}