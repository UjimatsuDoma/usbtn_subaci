package prac.tanken.shigure.ui.subaci.core.data

import org.junit.Test

import org.junit.Assert.*
import prac.tanken.shigure.ui.subaci.core.common.nio.readToString
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.model.category.Category
import java.net.URI

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun getCategories() {
        val categoryRegex = buildString {
            append("\\{\\n")
            append("\\t*className:\".+\",\\n")
            append("\\t*sectionId:\".+\",\\n")
            append("\\t*idList:\\[\\n")
            append("(\\s*,?\\{\"id\":\".+\",?\\s*}.*?\\n)+")
            append("\\t*\\]\\n")
            append("\\t*\\}")
        }.toRegex()
        println(categoryRegex.toString())

        val html = URI("https://leiros.cloudfree.jp/usbtn/usbtn.html").toURL().readToString()
        val matches = categoryRegex.findAll(html)
        println(matches.firstOrNull())
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
        println(categories)
    }
}