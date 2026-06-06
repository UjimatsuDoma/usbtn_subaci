package prac.tanken.shigure.ui.subaci.core.data.mock

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import prac.tanken.shigure.ui.subaci.core.common.io.readText
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.R
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.core.data.model.voices.Category

@Composable
fun voicesPreviewData(): List<Voice> {
    val res = LocalContext.current.resources
    val assets = LocalContext.current.assets

//    val voicesJson = res.openRawResource(R.raw.audio_list).readText()
//    val voices: List<Voice> = parseJsonString(voicesJson)
    val voicesJson = assets.open("subaciJson/audio_list.json").readText()
    val voices: List<Voice> = parseJsonString(voicesJson)

    return voices
}

@Composable
fun categoriesPreviewData(): List<Category> {
    val res = LocalContext.current.resources
    val assets = LocalContext.current.assets

//    val categoriesJson = res.openRawResource(R.raw.class_list).readText()
//    val categories: List<Category> = parseJsonString(categoriesJson)
    val categoriesJson = assets.open("subaciJson/class_list.json").readText()
    val categories: List<Category> = parseJsonString(categoriesJson)

    return categories
}

@Composable
fun sourcesPreviewData(): List<SourceEntity> {
    val res = LocalContext.current.resources
    val assets = LocalContext.current.assets

//    val sourcesJson = res.openRawResource(R.raw.video_list).readText()
//    val sources: List<SourceEntity> = parseJsonString(sourcesJson)
    val sourcesJson = assets.open("subaciJson/video_list.json").readText()
    val sources: List<SourceEntity> = parseJsonString(sourcesJson)

    return sources
}

