import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.jetbrains.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}

group = "prac.tanken.shigure.ui.subaci"
version = "unspecified"

dependencies {
    // Brother projects
    implementation(project(":common"))

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
}
