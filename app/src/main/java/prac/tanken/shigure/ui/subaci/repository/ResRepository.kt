package prac.tanken.shigure.ui.subaci.repository

import android.content.res.AssetManager
import android.content.res.Resources
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.data.util.readIS2Text
import javax.inject.Inject

class ResRepository @Inject constructor(
    val res: Resources,
    val am: AssetManager
) {
    val scope = CoroutineScope(Job())
    var voices: Flow<List<Voice>> = flowOf()

    init {
        test()
    }

    fun loadVoices(): Flow<List<Voice>> {
        val voicesJson = readIS2Text(res.openRawResource(R.raw.audio_list))
        val voices: List<Voice> = parseJsonString(voicesJson)
        return flowOf(voices)
    }

    fun test() = scope.launch(Dispatchers.IO) {
        val voicesJson = readIS2Text(res.openRawResource(R.raw.audio_list))
        val voices: List<Voice> = parseJsonString(voicesJson)
        this@ResRepository.voices = flowOf(voices)
    }

    fun getAFD(path: String) = am.openFd(path)
}