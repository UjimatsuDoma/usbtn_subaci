plugins {
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

group = "prac.tanken.shigure.ui.subaci"
version = "unspecified"

dependencies {
    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)

    // Apache Commons IO
    implementation(libs.commons.io)
}
