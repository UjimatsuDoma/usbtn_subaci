// Top-level build file where you can add configuration options common to all sub-projects/modules.
tasks.register("clean") {
    subprojects.forEach { project ->
        dependsOn(project.tasks.getByName("clean"))
    }
}