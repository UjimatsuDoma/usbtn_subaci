import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.logging.LogLevel

class HelloPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            println("Hello! This is a composite build.")
        }
    }
}