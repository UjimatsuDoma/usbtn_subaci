plugins {
    alias(libs.plugins.subaci.android.application)
    alias(libs.plugins.subaci.android.application.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci"
    compileSdk = 36

    defaultConfig {
        applicationId = "prac.tanken.shigure.ui.subaci"
        targetSdk = 36
        versionCode = 9
        versionName = "Milestone 3 Revision 3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.player)
    implementation(projects.core.ui)
    implementation(projects.feature.base)
    implementation(projects.feature.voices)
    implementation(projects.feature.sources)
    implementation(projects.feature.playlist)
    implementation(projects.feature.settings)
    implementation(projects.fontman)

    implementation(libs.androidx.activity.compose)

    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)

    // Compose ConstraintLayout
    implementation(libs.androidx.constraintlayout.compose)
    // SplashScreen API
    implementation(libs.androidx.core.splashscreen)

    // LeakCanary
    debugImplementation(libs.leakcanary.android)

}