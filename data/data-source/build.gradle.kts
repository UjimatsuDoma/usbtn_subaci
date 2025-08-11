plugins {
    alias(libs.plugins.subaci.resource.download)

    alias(libs.plugins.android.library)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.symbol.processing)
}

tasks.preBuild {
    dependsOn("downloadResource")
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.core.data"
    compileSdk = 35

    defaultConfig {
        minSdk = 21

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    buildFeatures {
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    room {
        schemaDirectory("$projectDir/room_schemas")
    }
}

group = "prac.tanken.shigure.ui.subaci"
version = "unspecified"

dependencies {
    // Brother projects
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.common)

    // Basic dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)

    // Kotlin Serialization
    implementation(libs.kotlinx.serialization.json)
    // Kotlin Reflection
    implementation(libs.kotlin.reflect)

    // DI stuff
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
}