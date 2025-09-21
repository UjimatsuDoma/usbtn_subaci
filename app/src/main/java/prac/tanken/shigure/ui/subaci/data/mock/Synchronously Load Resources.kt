package prac.tanken.shigure.ui.subaci.data.mock

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.R
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.core.data.model.voices.Category
import prac.tanken.shigure.ui.subaci.data.util.readIS2Text

@Composable
fun voicesPreviewData(): List<Voice> {
    val res = LocalContext.current.resources

    val voicesJson = readIS2Text(res.openRawResource(R.raw.audio_list))
    val voices: List<Voice> = parseJsonString(voicesJson)

    return voices
}

@Composable
fun categoriesPreviewData(): List<Category> {
    val res = LocalContext.current.resources

    val categoriesJson = readIS2Text(res.openRawResource(R.raw.class_list))
    val categories: List<Category> = parseJsonString(categoriesJson)

    return categories
}

@Composable
fun sourcesPreviewData(): List<SourceEntity> {
    val res = LocalContext.current.resources

    val sourcesJson = readIS2Text(res.openRawResource(R.raw.video_list))
    val sources: List<SourceEntity> = parseJsonString(sourcesJson)

    return sources
}

