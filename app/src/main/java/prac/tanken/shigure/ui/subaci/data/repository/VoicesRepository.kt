package prac.tanken.shigure.ui.subaci.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.data.di.DailyVoiceDataStore
import prac.tanken.shigure.ui.subaci.data.di.VoicesDataStore
import prac.tanken.shigure.ui.subaci.data.di.VoicesGroupedByJson
import prac.tanken.shigure.ui.subaci.data.helper.DailyVoiceHelper.todayStr
import prac.tanken.shigure.ui.subaci.data.model.voices.DailyVoiceEntity
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.data.preferences.DailyVoiceKeys
import prac.tanken.shigure.ui.subaci.data.preferences.VoicesKeys
import javax.inject.Inject

class VoicesRepository @Inject constructor(
    @VoicesGroupedByJson val voicesGroupedByJson: Json,
    @VoicesDataStore val voicesDataStore: DataStore<Preferences>,
    @DailyVoiceDataStore val dailyVoiceDataStore: DataStore<Preferences>
) {
    val dailyVoiceEntityFlow: Flow<DailyVoiceEntity?> = dailyVoiceDataStore.data
        .map { preference ->
            val jsonString = preference[DailyVoiceKeys.DAILY_VOICE]
            jsonString?.let { Json.decodeFromString(it) }
        }

    val voicesGroupedByFlow: Flow<VoicesGroupedBy?> = voicesDataStore.data.map { preference ->
        val jsonString = preference[VoicesKeys.VOICES_GROUPED_BY]
        jsonString?.let { voicesGroupedByJson.decodeFromString(it) }
    }

    suspend fun updateDailyVoice(voiceId: String) {
        dailyVoiceDataStore.edit { preference ->
            wipeM2R3DailyVoicePreferences()
            val dailyVoiceEntity = DailyVoiceEntity(voiceId, todayStr)
            preference[DailyVoiceKeys.DAILY_VOICE] = Json.encodeToString(dailyVoiceEntity)
        }
    }

    suspend fun updateVoicesGroupedBy(voicesGroupedBy: VoicesGroupedBy) {
        voicesDataStore.edit { preference ->
            preference[VoicesKeys.VOICES_GROUPED_BY] = Json.encodeToString(voicesGroupedBy)
        }
    }

    suspend fun wipeM2R3DailyVoicePreferences() {
        voicesDataStore.edit { preference ->
            preference.remove(stringPreferencesKey("voice_id"))
            preference.remove(stringPreferencesKey("add_date"))
        }
    }
}