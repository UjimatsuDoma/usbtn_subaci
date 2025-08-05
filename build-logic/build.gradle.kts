tasks.register("clean") {
    subprojects.forEach { project ->
        dependsOn(project.tasks.getByName("clean"))
    }
}