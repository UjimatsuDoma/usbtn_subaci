package prac.tanken.shigure.ui.subaci.build_logic.convention

import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply

internal fun Project.configureCommonDependencies() {
    // Kotlin Serialization
    apply(plugin = libs.findPlugin("kotlin-serialization").get().get().pluginId)
    dependencies.add("implementation", libs.findLibrary("kotlinx-serialization-json").get())
    // KSP
    apply(plugin = libs.findPlugin("kotlin-symbol-processing").get().get().pluginId)
}

internal fun Project.configureAndroidCommonDependencies() {
    // Dagger Hilt
    apply(plugin = libs.findPlugin("android-hilt").get().get().pluginId)
    dependencies.add("implementation", libs.findLibrary("hilt-android").get())
    dependencies.add("ksp", libs.findLibrary("hilt-android-compiler").get())
}