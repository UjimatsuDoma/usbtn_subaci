import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
    alias(libs.plugins.subaci.resource.download)

    alias(libs.plugins.androidx.room)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.core.data"

    room {
        schemaDirectory("$projectDir/schemas")
    }

    sourceSets {
        getByName("main") {
            assets.srcDir(project.layout.buildDirectory.dir("subaciTmp/assets").get().asFile.absolutePath)
        }
    }
}

tasks.preBuild {
    dependsOn(tasks.named("logDownloadFin"))
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.ui)

    // Kotlin Reflect
    implementation(libs.kotlin.reflect)

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
}