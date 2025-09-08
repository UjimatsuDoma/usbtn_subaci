import org.gradle.api.Plugin
import org.gradle.api.Project

class HelloPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            println("Hello! This is a composite build.")
        }
    }
}