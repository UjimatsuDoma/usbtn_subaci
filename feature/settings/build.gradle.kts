import java.time.ZoneId
import java.time.ZonedDateTime
import kotlin.apply

plugins {
    alias(libs.plugins.subaci.android.library)
    alias(libs.plugins.subaci.android.library.compose)
}

android {
    namespace = "prac.tanken.shigure.ui.subaci.settings"

    buildTypes {
        val now = buildString {
            ZonedDateTime.now(ZoneId.systemDefault()).apply {
                append(String.format("%d%02d%02d", year, monthValue, dayOfMonth))
                append("-")
                append(String.format("%02d%02d%02d", hour, minute, second))
            }
        }

        debug {
            buildConfigField("String", "BUILT_IN_COLOR_SOURCE", "\"22523677\"")
            buildConfigField("String", "BUILD_TIME", "\"$now\"")
            buildConfigField("String", "VERSION_NAME", "\"Milestone 3 Revision 2\"")
        }
        release {
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
    implementation(projects.feature.base)
    implementation(libs.subaci.core.model)
    implementation(libs.subaci.core.common.android)
    implementation(libs.subaci.data.source)
}