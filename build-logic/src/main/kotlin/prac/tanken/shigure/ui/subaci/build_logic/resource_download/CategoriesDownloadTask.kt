package prac.tanken.shigure.ui.subaci.build_logic.resource_download

import kotlinx.serialization.Serializable
import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.InputDirectory
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import prac.tanken.shigure.ui.subaci.build_logic.common.encodeJsonString
import prac.tanken.shigure.ui.subaci.build_logic.common.parseJsonString
import prac.tanken.shigure.ui.subaci.build_logic.common.readToString
import java.io.FileWriter
import java.net.URI

abstract class CategoriesDownloadTask : DefaultTask() {
    @get:InputDirectory
    abstract val destination: DirectoryProperty

    fun fetchCategories(): List<Category> {
        val html = URI(HTML_URL).toURL().readToString()
        val matches = categoryRegex.findAll(html)
        val categories = matches.map {
            val categoryJson = it.groupValues[0]
                // add quotation mark
                .replace("className", "\"className\"")
                .replace("sectionId", "\"sectionId\"")
                .replace("idList", "\"idList\"")
                // remove comments
                .replace("//.*\\n".toRegex(), "\n")
                // remove trailing commas
                .replace(",\\s*}".toRegex(), "}")
            parseJsonString<Category>(categoryJson)
        }.toList()
        return categories
    }

    @TaskAction
    fun run() {
        val file = destination.asFile.get()

        for(currentTry in 0 until 10) {
            try {
                println("fetching categories json...")
                val categories = fetchCategories()
                FileWriter("$file/src/main/res/raw/class_list.json").use {
                    it.write(encodeJsonString(categories))
                }
                break
            } catch (e: Exception) {
                e.printStackTrace()
                logger.error("ERROR RETRY: ${currentTry + 1}/10")
                Thread.sleep(2000)
            }
        }
    }

    @Serializable
    data class Category (
        val className: String,
        val sectionId: String,
        val idList: List<VoiceReference>
    )

    @Serializable
    data class VoiceReference(val id: String)
}