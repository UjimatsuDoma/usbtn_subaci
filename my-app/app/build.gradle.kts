import org.gradle.kotlin.dsl.release
import java.time.ZoneId
import java.time.ZonedDateTime

plugins {
    alias(libs.plugins.subaci.android.application)
    alias(libs.plugins.subaci.android.application.compose)

//    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci"

    defaultConfig {
        applicationId = "prac.tanken.shigure.ui.subaci"

        versionCode = 8
        versionName = "Milestone 3 Revision 2"
    }

    signingConfigs {
        create("release") {
            storeFile = file("./tanken-keystore.jks")
            storePassword = "tankenTaisai"
            keyAlias = "tanken"
            keyPassword = "tanken"
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isDebuggable = false
            signingConfig = signingConfigs.named("release").get()

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    // Other projects in the GREATER PROJECT
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.ui)
    implementation(libs.subaci.data.source)
    implementation(libs.subaci.feature.base)
    implementation(libs.subaci.feature.voices)
    implementation(libs.subaci.feature.sources)
    implementation(libs.subaci.feature.playlist)
    implementation(libs.subaci.feature.settings)

    // Compose
    implementation(libs.androidx.activity.compose)
    // KotlinX DateTime
    implementation(libs.kotlinx.datetime)
    // Kotlin Reflection
    implementation(libs.kotlin.reflect)

    // SplashScreen API
    implementation(libs.androidx.core.splashscreen)
}