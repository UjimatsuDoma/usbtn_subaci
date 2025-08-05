plugins {
    id("base")
}

tasks {
    register("customClean") {
        dependsOn(gradle.includedBuild("my-app").task(":clean"))
        dependsOn(gradle.includedBuild("core").task(":clean"))
        dependsOn(gradle.includedBuild("build-logic").task(":clean"))
    }

    clean {
        dependsOn("customClean")
    }
}