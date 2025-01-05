import org.apache.commons.io.FileUtils
import prac.tanken.shigure.ui.subaci.build_src.BASE_URL
import prac.tanken.shigure.ui.subaci.build_src.getCategories
import prac.tanken.shigure.ui.subaci.build_src.getVoices
import java.io.File
import java.io.FileWriter
import java.net.URI

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.0"
    id("androidx.room")
}

android {
    namespace = "prac.tanken.shigure.ui.subaci"
    compileSdk = 35

    defaultConfig {
        applicationId = "prac.tanken.shigure.ui.subaci"
        minSdk = 21
        targetSdk = 35
        versionCode = 6
        versionName = "Milestone 3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    flavorDimensions += "api"
    productFlavors {
        create("preOreo") {
            dimension = "api"
            minSdk = 21
            maxSdk = 25
        }
        create("modern") {
            dimension = "api"
            minSdk = 26
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    // Kotlin Reflect
    implementation(libs.kotlin.reflect)
    // KotlinX DateTime
    implementation(libs.kotlinx.datetime)

    // DI stuff
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Jetpack Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)

    // Microsoft Fluent Design Icons
    implementation(libs.fluent.system.icons)
    // Google Material Icons
    implementation(libs.androidx.material.icons.core)
    implementation(libs.androidx.material.icons.extended)

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

}

tasks.register("downloadResource") {
    doLast {
        var tries = 0
        while (tries < 10) {
            try {
                val htmlUrl = "$BASE_URL/usbtn.html"
                val targetPath = "${projectDir}/build/downloadedResources/usbtn.txt"
                FileUtils.copyURLToFile(URI(htmlUrl).toURL(), File(targetPath))

                val voices = getVoices(targetPath, "${projectDir}/src/main/assets/subaciAudio/")
                FileWriter("${projectDir}/src/main/res/raw/audio_list.json").use { it.write(voices) }
                val categories = getCategories(targetPath)
                FileWriter("${projectDir}/src/main/res/raw/class_list.json").use {
                    it.write(
                        categories
                    )
                }
                break
            } catch (e: Exception) {
                e.printStackTrace()
                println("ERROR RETRY: ${tries + 1}/10")
                tries++
                Thread.sleep(2000)
                continue
            }
        }
    }
}

tasks.preBuild {
    dependsOn("downloadResource")
}