import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
    alias(libs.plugins.kotlin.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    // Other projects
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
