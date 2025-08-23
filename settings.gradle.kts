pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ShigureUiButtonAppComposeImplementation"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
includeBuild("build-logic")
includeBuild("core")
includeBuild("data")
includeBuild("feature")
includeBuild("my-app")
