package prac.tanken.shigure.ui.subaci.core.data.mock

import prac.tanken.shigure.ui.subaci.core.common.io.readText
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.BuildConfig
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.core.data.model.voices.Category
import java.io.File


fun voicesPreviewData(): List<Voice> {
    val voicesJson = File(BuildConfig.SUBACI_JSON_DIR, "audio_list.json").inputStream().readText()
    val voices: List<Voice> = parseJsonString(voicesJson)

    return voices
}

fun categoriesPreviewData(): List<Category> {
    val categoriesJson = File(BuildConfig.SUBACI_JSON_DIR, "class_list.json").inputStream().readText()
    val categories: List<Category> = parseJsonString(categoriesJson)

    return categories
}

fun sourcesPreviewData(): List<SourceEntity> {
    val sourcesJson = File(BuildConfig.SUBACI_JSON_DIR, "video_list.json").inputStream().readText()
    val sources: List<SourceEntity> = parseJsonString(sourcesJson)

    return sources
}

