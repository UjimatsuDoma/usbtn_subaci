import com.android.build.gradle.internal.tasks.factory.dependsOn
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.checkIfTempDirExist
import prac.tanken.shigure.ui.subaci.build_logic.resource_download.downloadSources

plugins {
    alias(libs.plugins.subaci.android.application)
    alias(libs.plugins.subaci.android.application.compose)
    alias(libs.plugins.subaci.resource.download)
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

    sourceSets {
        getByName("main") {
            assets.srcDir(project.layout.buildDirectory.dir("subaciTmp/assets").get().asFile.absolutePath)
        }
    }
}

tasks {
    downloadVoices.dependsOn(checkIfTempDirExist)
    downloadSources.dependsOn(checkIfTempDirExist)
    downloadCategories.dependsOn(checkIfTempDirExist)
    val log = register("log") {
        doLast {
            logger.info("RESOURCE DOWNLOAD FINISHED")
        }
    }
    log.dependsOn(downloadVoices, downloadSources, downloadCategories)
    preBuild.dependsOn(log)
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