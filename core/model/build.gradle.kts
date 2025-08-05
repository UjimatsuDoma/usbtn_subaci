import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    //在该模块内编写构建插件所必需的依赖。
    //给App的核心实体类单开模块的一个重要原因是，这玩意不能跟安卓插件共存。
    `kotlin-dsl`

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

gradlePlugin {
    plugins {
        register("SUBACIResourceDownload") {
            id = libs.plugins.subaci.resource.download.get().pluginId
            implementationClass = "prac.tanken.shigure.ui.subaci.data.model.plugin.ResourceDownloadPlugin"
        }
    }
}
