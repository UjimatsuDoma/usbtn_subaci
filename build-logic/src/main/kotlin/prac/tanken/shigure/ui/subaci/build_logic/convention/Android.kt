package prac.tanken.shigure.ui.subaci.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.jsonDir
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.thumbnailsDir
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.voicesDir
import java.time.ZoneId
import java.time.ZonedDateTime

internal fun Project.configureAndroidBase(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        val now = buildString {
            ZonedDateTime.now(ZoneId.systemDefault()).apply {
                append(String.format("%d%02d%02d", year, monthValue, dayOfMonth))
                append("-")
                append(String.format("%02d%02d%02d", hour, minute, second))
            }
        }

        buildTypes {
            configureEach {
                buildConfigField("String", "BUILT_IN_COLOR_SOURCE", "\"22523677\"")
                buildConfigField("String", "BUILD_TIME", "\"$now\"")
                buildConfigField("String", "VERSION_NAME", "\"Milestone 3 Revision 4\"")
                buildConfigField("String", "SUBACI_JSON_DIR", "\"${jsonDir.absolutePath.replace("\\", "/")}\"")
                buildConfigField("String", "SUBACI_VOICES_DIR", "\"${voicesDir.absolutePath.replace("\\", "/")}\"")
                buildConfigField("String", "SUBACI_THUMBNAILS_DIR", "\"${thumbnailsDir.absolutePath.replace("\\", "/")}\"")
            }
        }
        buildFeatures {
            buildConfig = true
        }

        dependencies {
            add("implementation", libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
            add("implementation", libs.findLibrary("androidx-core-ktx").get())
            add("implementation", libs.findLibrary("androidx-appcompat").get())
            add("implementation", libs.findLibrary("material").get())
            add("testImplementation", libs.findLibrary("junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx-junit").get())
            add("androidTestImplementation", libs.findLibrary("androidx-espresso-core").get())
        }
    }
}