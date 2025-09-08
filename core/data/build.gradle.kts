plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
    alias(libs.plugins.subaci.resource.download)
}

tasks {
    preBuild {
        dependsOn(named("downloadVoices"))
        dependsOn(named("downloadCategories"))
        dependsOn(named("downloadSources"))
        dependsOn(named("checkIfFilesExist"))
    }
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.core.data"
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