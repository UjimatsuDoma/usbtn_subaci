package prac.tanken.shigure.ui.subaci.data.repository

import android.content.res.AssetManager
import android.content.res.Resources
import androidx.annotation.StringRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.model.voices.Category
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.data.util.readIS2Text
import javax.inject.Inject

class ResRepository @Inject constructor(
    val res: Resources,
    val am: AssetManager,
) {
    suspend fun loadVoices(): List<Voice> = withContext(Dispatchers.IO) {
        val voicesJson = readIS2Text(res.openRawResource(R.raw.audio_list))
        val voices: List<Voice> = parseJsonString(voicesJson)
        return@withContext voices
    }

    suspend fun loadCategories(): List<Category> = withContext(Dispatchers.IO) {
        val categoriesJson = readIS2Text(res.openRawResource(R.raw.class_list))
        val categories: List<Category> = parseJsonString(categoriesJson)
        return@withContext categories
    }

    suspend fun loadSources(): List<SourceEntity> = withContext(Dispatchers.IO) {
        val sourcesJson = readIS2Text(res.openRawResource(R.raw.video_list))
        val sources: List<SourceEntity> = parseJsonString(sourcesJson)
        return@withContext sources
    }

    fun stringRes(@StringRes stringRes: Int) = res.getString(stringRes)

    fun getVoiceAFD(vr: VoiceReference) = am.openFd("subaciAudio/${vr.id}.mp3")

    fun getThumbAFD(sourceEntity: SourceEntity) = am.openFd("subaciThumbs/${sourceEntity.videoId}.jpg")
}