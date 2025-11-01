plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
    alias(libs.plugins.kotlin.parcelize)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.core.ui"
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
}