import java.time.ZoneId
import java.time.ZonedDateTime

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.android.hilt)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.android)
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
        val now = buildString {
            ZonedDateTime.now(ZoneId.systemDefault()).apply {
                append(String.format("%d%02d%02d", year, monthValue, dayOfMonth))
                append("-")
                append(String.format("%02d%02d%02d", hour, minute, second))
            }
        }

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
    // Child projects
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.data.source)

    // Basic Android library dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    // Compose ConstraintLayout
    implementation(libs.androidx.constraintlayout.compose)

    // AndroidX Lifecycle
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    // Kotlin Reflection
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