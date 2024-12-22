package prac.tanken.shigure.ui.subaci.data.repository

import android.content.res.Resources
import androidx.annotation.StringRes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.model.Category
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.data.util.readIS2Text
import javax.inject.Inject

class ResRepository @Inject constructor(
    val res: Resources,
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

    fun stringRes(@StringRes stringRes: Int) = res.getString(stringRes)
}