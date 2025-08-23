plugins {
    alias(libs.plugins.subaci.jvm.library)
}

dependencies {
    // Kotlin DateTime
    implementation(libs.kotlinx.datetime)

    // Apache Commons IO
    implementation(libs.commons.io)
}
