import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("plugin.serialization") version "2.1.20"
    `kotlin-dsl`
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
    implementation(libs.gradle)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.commons.io)
}

gradlePlugin {
    plugins.apply {
        register("hello-world-plugin") {
            id = "hello-world-plugin"
            implementationClass = "HelloWorldPlugin"
        }
        register("applicationFlavorConfig") {
            id = libs.plugins.subaci.application.flavor.get().pluginId
            implementationClass = "ApplicationFlavorPlugin"
        }
        register("libraryConventionConfig") {
            id = libs.plugins.subaci.library.convention.get().pluginId
            implementationClass = "LibraryConventionPlugin"
        }
        register("resourceTaskConfig") {
            id = libs.plugins.subaci.resource.task.configuration.get().pluginId
            implementationClass = "ResourceTaskPlugin"
        }
    }
}