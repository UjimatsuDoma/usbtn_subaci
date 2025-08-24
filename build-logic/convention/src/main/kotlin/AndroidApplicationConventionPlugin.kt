import com.android.build.api.dsl.ApplicationBaseFlavor
import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import prac.tanken.shigure.ui.subaci.build_logic.convention.AppFlavor
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureAndroidBase
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureAndroidCommonDependencies
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureCommonDependencies
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureFlavors
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureKotlinAndroid
import prac.tanken.shigure.ui.subaci.build_logic.convention.configureSdkVersion
import prac.tanken.shigure.ui.subaci.build_logic.convention.libs

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = libs.findPlugin("android-application").get().get().pluginId)
            apply(plugin = libs.findPlugin("kotlin-android").get().get().pluginId)

            group = "prac.tanken.shigure.ui.subaci"
            version = "unspecified"

            extensions.configure<ApplicationExtension> {
                configureFlavors(this) {
                    configureSdkVersion(it)
                }
                configureAndroidBase(this)
                configureKotlinAndroid(this)

                buildTypes {
                    release {
                        proguardFiles(
                            getDefaultProguardFile("proguard-android-optimize.txt"),
                            "proguard-rules.pro"
                        )
                    }
                }

                buildFeatures {
                    buildConfig = true
                }
            }

            configureCommonDependencies()
            configureAndroidCommonDependencies()
        }
    }
}