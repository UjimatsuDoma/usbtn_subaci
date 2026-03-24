package prac.tanken.shigure.ui.subaci.fontman.core

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import prac.tanken.chaquo.fonttools.generateStaticFontFromVariableByWeight
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoCJKLocale
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoStyle
import java.io.File
import javax.inject.Inject

class FontInstantiateRepository @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private val fontsDir = File(context.filesDir, "fonts")

    val inputNames = NotoStyle.entries.flatMap { style ->
        NotoCJKLocale.entries.map { locale ->
            "${style.fileName}_${locale.code}"
        }
    }
    val total = inputNames.size * 9
    var current = 0

    suspend fun instantiateAllWeightsWithProgress(onProgress: (Float) -> Unit) {
        inputNames.flatMap { s ->
            val inputFont = File(fontsDir, "$s.ttf")
            if (!inputFont.exists()) throw RuntimeException("字体不存在，静态化步骤无法进行")
            (1..9).map { k ->
                val weight = k * 100
                val outputFont = File(fontsDir, "${s}_$weight.ttf")
                withContext(Dispatchers.IO) {
                    context.generateStaticFontFromVariableByWeight(
                        inputFont.absolutePath, outputFont.absolutePath, weight
                    )
                    current++
                    println("$current $total")
                    onProgress(current.toFloat() / total.toFloat())
                }
            }
        }
    }
}