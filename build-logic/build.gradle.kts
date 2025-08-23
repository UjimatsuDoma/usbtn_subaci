plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

tasks.register("clean") {
    subprojects.forEach { project ->
        dependsOn(project.tasks.getByName("clean"))
    }
}