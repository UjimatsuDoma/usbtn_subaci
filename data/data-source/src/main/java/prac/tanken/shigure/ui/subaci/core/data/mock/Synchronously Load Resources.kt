package prac.tanken.shigure.ui.subaci.core.data.mock

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import prac.tanken.shigure.ui.subaci.core.common.io.readText
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.R
import prac.tanken.shigure.ui.subaci.core.data.model.category.Category
import prac.tanken.shigure.ui.subaci.core.data.model.source.Source
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice

@Composable
fun voicesPreviewData(): List<Voice> {
    val res = LocalContext.current.resources

    val voicesJson = res.openRawResource(R.raw.audio_list).readText()
    val voices: List<Voice> = parseJsonString(voicesJson)

    return voices
}

@Composable
fun categoriesPreviewData(): List<Category> {
    val res = LocalContext.current.resources

    val categoriesJson = res.openRawResource(R.raw.class_list).readText()
    val categories: List<Category> = parseJsonString(categoriesJson)

    return categories
}

@Composable
fun sourcesPreviewData(): List<Source> {
    val res = LocalContext.current.resources

    val sourcesJson = res.openRawResource(R.raw.video_list).readText()
    val sources: List<Source> = parseJsonString(sourcesJson)

    return sources
}

