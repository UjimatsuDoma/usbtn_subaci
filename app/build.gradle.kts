import java.time.ZoneId
import java.time.ZonedDateTime

val now = buildString {
    ZonedDateTime.now(ZoneId.systemDefault()).apply {
        append(String.format("%d%02d%02d", year, monthValue, dayOfMonth))
        append("-")
        append(String.format("%02d%02d%02d", hour, minute, second))
    }
}

plugins {
    alias(libs.plugins.subaci.android.application)

    alias(libs.plugins.android.hilt)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.symbol.processing)
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
        versionCode = 8
        versionName = "Milestone 3 Revision 2"

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
    buildFeatures {
        compose = true
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(libs.androidx.activity.compose)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    // Kotlin Reflect
    implementation(libs.kotlin.reflect)
    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)

    // DI stuff
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

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