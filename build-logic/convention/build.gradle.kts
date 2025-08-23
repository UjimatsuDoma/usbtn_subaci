import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

dependencies {
    // Necessary for writing plugins
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.kotlin.gradle.plugin)
}

gradlePlugin {
    plugins {
        // OHAYOU SEKAI GOOD MORNING WORLD!!
        register("SUBACIHello") {
            id = libs.plugins.subaci.hello.get().pluginId
            implementationClass = "HelloPlugin"
        }
        // Convention plugins
        register("SUBACIAndroidApplicationConvention") {
            id = libs.plugins.subaci.android.application.asProvider().get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("SUBACIAndroidApplicationComposeConvention") {
            id = libs.plugins.subaci.android.application.compose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("SUBACIAndroidLibraryConvention") {
            id = libs.plugins.subaci.android.library.asProvider().get().pluginId
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("SUBACIAndroidLibraryComposeConvention") {
            id = libs.plugins.subaci.android.library.compose.get().pluginId
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("SUBACIJvmLibraryConvention") {
            id = libs.plugins.subaci.jvm.library.get().pluginId
            implementationClass = "JvmLibraryConventionPlugin"
        }
    }
}
