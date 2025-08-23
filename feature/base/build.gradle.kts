plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.base"
}

dependencies {
    // Other projects
    implementation(libs.subaci.core.common)
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.ui)
    implementation(libs.subaci.data.source)
}