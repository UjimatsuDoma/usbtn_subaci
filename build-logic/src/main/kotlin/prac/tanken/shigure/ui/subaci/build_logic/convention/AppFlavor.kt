package prac.tanken.shigure.ui.subaci.build_logic.convention

import com.android.build.api.dsl.ApplicationBaseFlavor
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor
import org.gradle.api.Project

enum class FlavorDimension(val value: String) {
    API("api")
}

enum class AppFlavor(
    val value: String,
    val dimension: FlavorDimension
) {
    MODERN(value = "modern", dimension = FlavorDimension.API),
    PRE_OREO(value = "preOreo", dimension = FlavorDimension.API)
}

fun Project.configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
    flavorConfigurationBlock: ProductFlavor.(flavor: AppFlavor) -> Unit = {},
) {
    commonExtension.apply {
        FlavorDimension.values().forEach { flavorDimension ->
            flavorDimensions += flavorDimension.value
        }

        productFlavors {
            AppFlavor.values().forEach { appFlavor ->
                register(appFlavor.value) {
                    dimension = appFlavor.dimension.value
                    flavorConfigurationBlock(this, appFlavor)
                    configureDependenciesByFlavor(appFlavor, this@apply)
                }
            }
        }
    }
}

fun ProductFlavor.configureSdkVersion(appFlavor: AppFlavor) {
    when (appFlavor) {
        AppFlavor.MODERN -> {
            minSdk = 26
        }

        AppFlavor.PRE_OREO -> {
            minSdk = 21
            if (this is ApplicationBaseFlavor) {
                maxSdk = 25
            }
        }
    }
}

fun Project.configureDependenciesByFlavor(
    appFlavor: AppFlavor,
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    when(appFlavor) {
        AppFlavor.MODERN -> {

        }
        AppFlavor.PRE_OREO -> {

        }
    }
}