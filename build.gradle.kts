plugins {
    id("base")
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.jetbrains.kotlin.jvm) apply false
}

tasks {
    register("customClean") {
        dependsOn(gradle.includedBuild("my-app").task(":clean"))
        dependsOn(gradle.includedBuild("data").task(":clean"))
        dependsOn(gradle.includedBuild("core").task(":clean"))
        dependsOn(gradle.includedBuild("build-logic").task(":clean"))
    }

    clean {
        dependsOn("customClean")
    }
}