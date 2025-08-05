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

group = "prac.tanken.shigure.ui.subaci"
version = "unspecified"

dependencies {
    compileOnly("prac.tanken.shigure.ui.subaci:model")
    // Kotlin Serialization
    compileOnly(libs.kotlinx.serialization.json)
}

gradlePlugin {
    plugins {
        register("SUBACIHello") {
            id = libs.plugins.subaci.hello.get().pluginId
            implementationClass = "HelloPlugin"
        }
    }
}
