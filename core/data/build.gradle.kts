import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)

    alias(libs.plugins.androidx.room)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.core.data"

    room {
        schemaDirectory("$projectDir/schemas")
    }
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