plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.sources"
}

dependencies {
    implementation(projects.feature.base)
    implementation(libs.subaci.core.common)
    implementation(libs.subaci.core.common.android)
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.player)
    implementation(libs.subaci.core.ui)
    implementation(libs.subaci.data.source)

    // Coil Image Loader
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    // Metadata Extractor by Drew Noakes
    implementation(libs.metadata.extractor)
}