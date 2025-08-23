import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    alias(libs.plugins.subaci.hello)
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
    alias(libs.plugins.subaci.resource.download)

    alias(libs.plugins.androidx.room)
}

tasks.preBuild.dependsOn("downloadResource")
tasks.named("downloadResource").dependsOn("prepareLintJarForPublish")

android {
    namespace = "prac.tanken.shigure.ui.subaci.core.data"

    room {
        schemaDirectory("$projectDir/room_schemas")
    }
}

dependencies {
    // Other projects
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.common)

    // Kotlin Reflection
    implementation(libs.kotlin.reflect)

    // Room Database
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
    // Preferences DataStore
    implementation(libs.androidx.datastore.preferences)
}