import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.apply

plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.feature.settings"

    val now = buildString {
        ZonedDateTime.now(ZoneId.systemDefault()).apply {
            append(String.format("%d%02d%02d", year, monthValue, dayOfMonth))
            append("-")
            append(String.format("%02d%02d%02d", hour, minute, second))
        }
    }

    buildTypes {
        configureEach {
            buildConfigField("String", "BUILT_IN_COLOR_SOURCE", "\"22523677\"")
            buildConfigField("String", "BUILD_TIME", "\"$now\"")
            buildConfigField("String", "VERSION_NAME", "\"Milestone 3 Revision 2\"")
        }
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core.common)
    implementation(projects.core.data)
    implementation(projects.core.ui)
    implementation(projects.feature.base)
}