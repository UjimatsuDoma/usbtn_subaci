plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.feature.settings"
}

dependencies {
    implementation(projects.core.data)
    implementation(projects.core.ui)
}