import com.android.build.gradle.internal.tasks.factory.dependsOn

plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
    alias(libs.plugins.subaci.resource.download)

    alias(libs.plugins.androidx.room)
}

tasks {
    preBuild {
        setMustRunAfter(
            listOf(
                named("downloadVoices"),
                named("downloadCategories"),
                named("downloadSources"),
                named("checkIfFilesExist")
            )
        )
        named("downloadVoices").get().mustRunAfter(named("checkIfFilesExist"))
        named("downloadCategories").get().mustRunAfter(named("checkIfFilesExist"))
        named("downloadSources").get().mustRunAfter(named("checkIfFilesExist"))
        named("prepareLintJarForPublish").get().mustRunAfter(named("downloadVoices"))
        named("prepareLintJarForPublish").get().mustRunAfter(named("downloadCategories"))
        named("prepareLintJarForPublish").get().mustRunAfter(named("downloadSources"))
    }
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