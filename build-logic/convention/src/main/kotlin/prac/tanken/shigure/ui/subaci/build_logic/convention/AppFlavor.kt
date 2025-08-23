package prac.tanken.shigure.ui.subaci.build_logic.convention

import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.ProductFlavor

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

fun configureFlavors(
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
                }
            }
        }
    }
}