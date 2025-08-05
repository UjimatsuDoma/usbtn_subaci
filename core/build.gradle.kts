plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
}

tasks.register("clean") {
    subprojects.forEach { project ->
        dependsOn(project.tasks.getByName("clean"))
    }
}