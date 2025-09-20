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
    alias(libs.plugins.subaci.android.application.compose)

    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.parcelize)
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
        buildConfig = true
    }

    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(projects.feature.base)
    implementation(projects.feature.voices)
    implementation(projects.feature.sources)
    implementation(projects.feature.playlist)
    implementation(projects.feature.settings)

    implementation(libs.androidx.activity.compose)

    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)

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