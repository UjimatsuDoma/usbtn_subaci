pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        exclusiveContent {
            forRepository {
                maven("https://dl.bintray.com/omicronapps/7-Zip-JBinding-4Android")
            }
            filter {
                includeGroup("net.sf.sevenzipjbinding")
            }
        }
    }
}

rootProject.name = "ShigureUiButtonAppComposeImplementation"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":core:data")
include(":core:common")
include(":core:player")
include(":core:ui")
include(":feature:base")
include(":feature:voices")
include(":feature:sources")
include(":feature:playlist")
include(":feature:settings")
include(":fontman")

includeBuild("build-logic")