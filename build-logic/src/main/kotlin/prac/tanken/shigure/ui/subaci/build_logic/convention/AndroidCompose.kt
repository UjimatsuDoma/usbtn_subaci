package prac.tanken.shigure.ui.subaci.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }

        dependencies {
            // Jetpack Compose
            val bom = libs.findLibrary("androidx-compose-bom").get()
            add("implementation", platform(bom))
            add("implementation", libs.findLibrary("androidx-ui").get())
            add("implementation", libs.findLibrary("androidx-ui-graphics").get())
            add("implementation", libs.findLibrary("androidx-ui-tooling-preview").get())
            add("implementation", libs.findLibrary("androidx-material3").get())
            add("implementation", libs.findLibrary("androidx-lifecycle-runtime-compose").get())
            add("androidTestImplementation", platform(bom))
            add("androidTestImplementation", libs.findLibrary("androidx-ui-test-junit4").get())
            add("debugImplementation", libs.findLibrary("androidx-ui-tooling").get())
            add("debugImplementation", libs.findLibrary("androidx-ui-test-manifest").get())
            // Other libraries compatible with Compose and used across the UI sector of the app
            // Jetpack Navigation
            add("implementation", libs.findLibrary("androidx-hilt-navigation-compose").get())
            add("implementation", libs.findLibrary("androidx-navigation-compose").get())
            // Icon packs
            add("implementation", libs.findLibrary("fluent-system-icons").get())
            add("implementation", libs.findLibrary("androidx-material-icons-core").get())
            add("implementation", libs.findLibrary("androidx-material-icons-extended").get())
        }
    }
}