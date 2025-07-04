package util

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.ApplicationProductFlavor
import com.android.build.api.dsl.CommonExtension

sealed class FlavorDimension {
    abstract val name: String

    object Api : FlavorDimension() {
        override val name: String
            get() = this::class.java.simpleName
    }
}

sealed class ProductFlavor(
    val dimension: FlavorDimension,
    val minApi: Int? = null,
    val maxApi: Int? = null,
) {
    abstract val name: String

    object PreOreo : ProductFlavor(
        FlavorDimension.Api,
        minApi = 21,
        maxApi = 25,
    ) {
        override val name: String
            get() = this::class.java.simpleName
    }

    object Modern : ProductFlavor(
        FlavorDimension.Api,
        minApi = 26,
    ) {
        override val name: String
            get() = this::class.java.simpleName
    }
}

fun configureFlavors(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    commonExtension.apply {
        val flavorDimensionList = FlavorDimension::class.nestedClasses.map { it.objectInstance as FlavorDimension }
        flavorDimensionList.forEach {
            flavorDimensions += it.name
        }

        val flavors = ProductFlavor::class.nestedClasses.map { it.objectInstance as ProductFlavor }
        flavors.forEach {
            productFlavors {
                create(it.name) {
                    dimension = it.dimension.name
                    minSdk = it.minApi
                    if(this@apply is ApplicationExtension && this is ApplicationProductFlavor) {
                        maxSdk = it.maxApi
                    }
                }
            }
        }
    }
}
