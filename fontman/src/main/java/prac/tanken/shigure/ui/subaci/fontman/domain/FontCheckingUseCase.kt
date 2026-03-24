package prac.tanken.shigure.ui.subaci.fontman.domain

import prac.tanken.shigure.ui.subaci.core.data.repository.InternalStorageRepository
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoCJKLocale
import java.io.File
import javax.inject.Inject

/**
 * 检查应用字体资源是否已解压且可正常使用。
 * 可变字体命名标准：<style>_<locale>.ttf
 * 静态字体命名标准：<style>_<locale>_<weight>.ttf
 */
class FontCheckingUseCase @Inject constructor(
    private val internalStorageRepository: InternalStorageRepository,
) {
    val fontsDir = File(internalStorageRepository.filesDir, "fonts")

    fun checkVariable(): Boolean {
        var result = true
        val fontFiles = NotoStyle.entries.flatMap { notoStyle ->
            NotoCJKLocale.entries.map { notoCJKLocale ->
                File(fontsDir, "${notoStyle.fileName}_${notoCJKLocale.code}.ttf")
            }
        }
        for (fontFile in fontFiles) {
            if (!fontFile.exists()) {
                result = false
                break
            }
        }
        return result
    }

    fun clearVariable() {
        val fontFiles = NotoStyle.entries.flatMap { notoStyle ->
            NotoCJKLocale.entries.map { notoCJKLocale ->
                File(fontsDir, "${notoStyle.fileName}_${notoCJKLocale.code}.ttf")
            }
        }
        for (fontFile in fontFiles) {
            if (fontFile.exists()) fontFile.delete()
        }
    }

    fun checkStatic(): Boolean {
        var result = true
        val fontFiles = NotoStyle.entries.flatMap { notoStyle ->
            NotoCJKLocale.entries.flatMap { notoCJKLocale ->
                (1..9).map {
                    val weight = it * 100
                    File(fontsDir, "${notoStyle.fileName}_${notoCJKLocale.code}_$weight.ttf")
                }
            }
        }
        for (fontFile in fontFiles) {
            if (!fontFile.exists()) {
                result = false
                break
            }
        }
        return result
    }

    fun clearStatic() {
        NotoStyle.entries.flatMap { notoStyle ->
            NotoCJKLocale.entries.flatMap { notoCJKLocale ->
                (1..9).map {
                    val weight = it * 100
                    val fileName = "${notoStyle.fileName}_${notoCJKLocale.code}_$weight.ttf"
                    File(fontsDir, fileName).apply {
                        if (exists()) delete()
                    }
                }
            }
        }
    }
}