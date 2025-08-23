plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.playlist"
}

dependencies {
    implementation(projects.feature.base)
    implementation(libs.subaci.core.common)
    implementation(libs.subaci.core.common.resource)
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.player)
    implementation(libs.subaci.core.ui)
    implementation(libs.subaci.data.source)
}