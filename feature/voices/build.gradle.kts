plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.voices"
}

dependencies {
    implementation(projects.feature.base)
    implementation(projects.feature.playlist)
    implementation(libs.subaci.core.common)
    implementation(libs.subaci.core.common.android)
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.player)
    implementation(libs.subaci.core.ui)
    implementation(libs.subaci.data.source)
}