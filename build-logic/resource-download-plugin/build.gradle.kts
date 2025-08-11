plugins {
    `kotlin-dsl`
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {
    // Brother projects
    implementation(libs.subaci.core.common)
    implementation(libs.subaci.core.model)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
}

gradlePlugin {
    plugins {
        register("SUBACIResourceDownload") {
            id = libs.plugins.subaci.resource.download.get().pluginId
            implementationClass = "prac.tanken.shigure.ui.subaci.plugin.ResourceDownloadPlugin"
        }
    }
}
