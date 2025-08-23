plugins {
    alias(libs.plugins.subaci.android.library)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.core.player"
}

dependencies {
    implementation(projects.data.dataSource)
    implementation(libs.subaci.core.model)
}