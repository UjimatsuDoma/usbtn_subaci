package prac.tanken.shigure.ui.subaci.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import prac.tanken.shigure.ui.subaci.data.di.DailyVoiceDataStore
import prac.tanken.shigure.ui.subaci.data.model.DailyVoice
import prac.tanken.shigure.ui.subaci.data.preferences.DailyVoiceKeys
import javax.inject.Inject

class DailyVoiceRepository @Inject constructor(
    @DailyVoiceDataStore val dataStore: DataStore<Preferences>
) {
    val dailyVoiceFlow: Flow<DailyVoice?> = dataStore.data.map { preference ->
        val voiceId = preference[DailyVoiceKeys.VOICE_ID]
        val addDateString = preference[DailyVoiceKeys.ADD_DATE]
        if (voiceId != null && addDateString != null) {
            DailyVoice(voiceId, LocalDate.Formats.ISO_BASIC.parse(addDateString))
        } else {
            null
        }
    }

    suspend fun updateDailyVoice(voiceId: String) {
        dataStore.edit { preference->
            preference[DailyVoiceKeys.VOICE_ID] = voiceId
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            preference[DailyVoiceKeys.ADD_DATE] = LocalDate.Formats.ISO_BASIC.format(today)
        }
    }
}