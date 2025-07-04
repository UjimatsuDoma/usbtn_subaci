import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import util.configureFlavors
import util.now

class ApplicationFlavorPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
            }

            extensions.configure<ApplicationExtension> {
                configureFlavors(this)
                buildTypes {
                    debug {
                        isMinifyEnabled = false
                        isDebuggable = true

                        buildConfigField("String", "BUILT_IN_COLOR_SOURCE", "\"22523677\"")
                        buildConfigField("String", "BUILD_TIME", "\"$now\"")
                    }
                    release {
                        isMinifyEnabled = true
                        isDebuggable = false
                        isShrinkResources = true

                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )

                        buildConfigField("String", "BUILT_IN_COLOR_SOURCE", "\"22523677\"")
                        buildConfigField("String", "BUILD_TIME", "\"$now\"")
                    }
                }
            }
        }
    }
}