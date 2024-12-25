package prac.tanken.shigure.ui.subaci.data.repository

import android.os.Build
import androidx.datastore.core.DataStore
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class DailyVoiceRepository @Inject constructor(
    @DailyVoiceDataStore val dataStore: DataStore<Preferences>
) {
    companion object {
        val todayStr = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            fun encode(date: LocalDate) = LocalDate.Formats.ISO_BASIC.format(date)
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            encode(today)
        } else {
            fun encode(date: Date) = synchronized(this) {
                return@synchronized SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(date)
            }
            val today = Date()
            encode(today)
        }
    }

    val dailyVoiceFlow: Flow<DailyVoice?> = dataStore.data.map { preference ->
        val voiceId = preference[DailyVoiceKeys.VOICE_ID]
        val addDateString = preference[DailyVoiceKeys.ADD_DATE]
        if (voiceId != null && addDateString != null) {
            DailyVoice(voiceId, addDateString)
        } else {
            null
        }
    }

    suspend fun updateDailyVoice(voiceId: String) {
        dataStore.edit { preference->
            preference[DailyVoiceKeys.VOICE_ID] = voiceId
            preference[DailyVoiceKeys.ADD_DATE] = todayStr
        }
    }
}