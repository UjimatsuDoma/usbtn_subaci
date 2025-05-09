import prac.tanken.shigure.ui.subaci.build_src.BASE_URL
import prac.tanken.shigure.ui.subaci.build_src.downloadFileFromUrl
import prac.tanken.shigure.ui.subaci.build_src.encodeJsonString
import prac.tanken.shigure.ui.subaci.build_src.getCategories
import prac.tanken.shigure.ui.subaci.build_src.getMaxResolutionThumbUrl
import prac.tanken.shigure.ui.subaci.build_src.getSources
import prac.tanken.shigure.ui.subaci.build_src.getVoices
import prac.tanken.shigure.ui.subaci.build_src.now
import java.io.FileWriter

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "2.1.0"
    id("androidx.room")
    id("kotlin-parcelize")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xwhen-guards")
    }
}

android {
    namespace = "prac.tanken.shigure.ui.subaci"
    compileSdk = 35

    defaultConfig {
        applicationId = "prac.tanken.shigure.ui.subaci"
        minSdk = 21
        targetSdk = 35
        versionCode = 7
        versionName = "Milestone 3 Revision 1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true

            buildConfigField("String", "BUILT_IN_COLOR_SOURCE", "\"22523677\"")
            buildConfigField("String", "BUILD_TIME", "\"$now\"")
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            buildConfigField("String", "BUILT_IN_COLOR_SOURCE", "\"22523677\"")
            buildConfigField("String", "BUILD_TIME", "\"$now\"")
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
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
    // Basic dependencies
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
    implementation(libs.androidx.exifinterface)
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
    // Kotlin DateTime
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
    // Compose ConstraintLayout
    implementation(libs.androidx.constraintlayout.compose)
    // SplashScreen API
    implementation(libs.androidx.core.splashscreen)

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)

    // Coil Image Loader
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Metadata Extractor by Drew Noakes
    implementation(libs.metadata.extractor)

}

tasks.register("downloadResource") {
    doLast {
        var tries = 0
        while (tries < 10) {
            try {
                val htmlUrl = "$BASE_URL/usbtn.html"

                val voices = getVoices(htmlUrl)
                FileWriter("${projectDir}/src/main/res/raw/audio_list.json").use {
                    it.write(encodeJsonString(voices))
                }
                val voicePath = "${projectDir}/src/main/assets/subaciAudio/"
                voices.forEach { voice ->
                    voice.src.apply {
                        val relPath = substringAfter("/")
                        val fileName = substringAfterLast("/")
                        val downloadUrl = "$BASE_URL/$relPath"
                        val downloadDest = voicePath + fileName
                        val downloadFile = File(voicePath, fileName)
                        if (!downloadFile.exists()) {
                            downloadFileFromUrl(
                                downloadUrl,
                                downloadDest,
                                mapOf(
                                    "Referer" to "https://leiros.cloudfree.jp/usbtn/usbtn.html"
                                )
                            )
                        }
                    }
                }

                val categories = getCategories(htmlUrl)
                FileWriter("${projectDir}/src/main/res/raw/class_list.json").use {
                    it.write(encodeJsonString(categories))
                }

                val sources = getSources(htmlUrl)
                FileWriter("${projectDir}/src/main/res/raw/video_list.json").use {
                    it.write(encodeJsonString(sources))
                }
                val sourcesThumbnailPath = "${projectDir}/src/main/assets/subaciThumbs/"
                sources.forEach { source ->
                    val id = source.videoId
                    val fileName = "$id.jpg"
                    val downloadFile = File(sourcesThumbnailPath, fileName)
                    if (!downloadFile.exists()) {
                        val downloadUrl = getMaxResolutionThumbUrl(id)
                        downloadFileFromUrl(
                            downloadUrl,
                            sourcesThumbnailPath + fileName,
                        )
                    }
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