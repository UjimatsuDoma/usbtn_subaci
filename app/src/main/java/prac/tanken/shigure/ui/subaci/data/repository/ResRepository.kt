package prac.tanken.shigure.ui.subaci.data.repository

import android.content.res.AssetManager
import android.content.res.Resources
import androidx.annotation.StringRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.data.model.voices.Category
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.data.util.readIS2Text
import javax.inject.Inject

class ResRepository @Inject constructor(
    val res: Resources,
    val am: AssetManager,
) {
    private var _voicesFlow = MutableStateFlow<List<Voice>>(emptyList())
    val voicesFlow = _voicesFlow.asStateFlow()
    private var _categoriesFlow = MutableStateFlow<List<Category>>(emptyList())
    val categoriesFlow = _categoriesFlow.asStateFlow()
    private var _sourcesFlow = MutableStateFlow<List<SourceEntity>>(emptyList())
    val sourcesFlow = _sourcesFlow.asStateFlow()

    suspend fun loadVoices() = withContext(Dispatchers.IO) {
        val voicesJson = readIS2Text(res.openRawResource(R.raw.audio_list))
        val voices: List<Voice> = parseJsonString<MutableList<Voice>>(voicesJson).also {
            it.forEachIndexed { index, voice ->
                if (voice.a == "AS" || voice.a == "ZA") it[index] = voice.copy(a = "SA")
            }
        }
        _voicesFlow.value = voices
    }

    suspend fun loadCategories() = withContext(Dispatchers.IO) {
        val categoriesJson = readIS2Text(res.openRawResource(R.raw.class_list))
        val categories: List<Category> = parseJsonString(categoriesJson)
        _categoriesFlow.value = categories
    }

    suspend fun loadSources() = withContext(Dispatchers.IO) {
        val sourcesJson = readIS2Text(res.openRawResource(R.raw.video_list))
        val sources: List<SourceEntity> = parseJsonString(sourcesJson)
        _sourcesFlow.value = sources
    }

    fun stringRes(@StringRes stringRes: Int) = res.getString(stringRes)

    fun getVoiceAFD(vr: VoiceReference) = am.openFd("subaciAudio/${vr.id}.mp3")
}