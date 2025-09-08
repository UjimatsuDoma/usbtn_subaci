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
    }
}

rootProject.name = "Shigure Ui Button App Compose Implementation"
include(":app")
includeBuild("build-logic")
include(":core:data")
include(":core:common")
include(":core:ui")
include(":feature:base")
include(":feature:voices")
include(":feature:sources")
include(":feature:playlist")
include(":feature:settings")
