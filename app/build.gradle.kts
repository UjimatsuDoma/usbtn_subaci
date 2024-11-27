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
}

android {
    namespace = "prac.tanken.shigure.ui.subaci"
    compileSdk = 35

    defaultConfig {
        applicationId = "prac.tanken.shigure.ui.subaci"
        minSdk = 23
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
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
}

tasks.register("downloadResource") {
    doLast {
        val htmlUrl = BASE_URL + "usbtn.html"
        val targetPath = "${projectDir}/build/downloadedResources/usbtn.txt"
        FileUtils.copyURLToFile(URI(htmlUrl).toURL(), File(targetPath))

        val voices = getVoices(targetPath, "${projectDir}/src/main/assets/subaciAudio/")
        val categories = getCategories(targetPath)
        FileWriter("${projectDir}/src/main/res/raw/audio_list.json").use { it.write(voices) }
        FileWriter("${projectDir}/src/main/res/raw/class_list.json").use { it.write(categories) }
    }
}

tasks.preBuild {
    dependsOn("downloadResource")
}