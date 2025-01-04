package prac.tanken.shigure.ui.subaci.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.data.di.DailyVoiceDataStore
import prac.tanken.shigure.ui.subaci.data.di.VoicesDataStore
import prac.tanken.shigure.ui.subaci.data.di.VoicesGroupedByJson
import prac.tanken.shigure.ui.subaci.data.model.voices.DailyVoice
import prac.tanken.shigure.ui.subaci.data.preferences.DailyVoiceKeys
import prac.tanken.shigure.ui.subaci.data.helper.DailyVoiceHelper.todayStr
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.data.preferences.VoicesKeys
import javax.inject.Inject

class VoicesRepository @Inject constructor(
    @VoicesGroupedByJson val voicesGroupedByJson: Json,
    @VoicesDataStore val voicesDataStore: DataStore<Preferences>,
    @DailyVoiceDataStore val dailyVoiceDataStore: DataStore<Preferences>
) {
    val dailyVoiceFlow: Flow<DailyVoice?> = dailyVoiceDataStore.data.map { preference ->
        val voiceId = preference[DailyVoiceKeys.VOICE_ID]
        val addDateString = preference[DailyVoiceKeys.ADD_DATE]
        if (voiceId != null && addDateString != null) {
            DailyVoice(voiceId, addDateString)
        } else {
            null
        }
    }

    val voicesGroupedByFlow: Flow<VoicesGroupedBy?> = voicesDataStore.data.map { preference ->
        val jsonString = preference[VoicesKeys.VOICES_GROUPED_BY]
        jsonString?.let { voicesGroupedByJson.decodeFromString(it) }
    }

    suspend fun updateDailyVoice(voiceId: String) {
        dailyVoiceDataStore.edit { preference ->
            preference[DailyVoiceKeys.VOICE_ID] = voiceId
            preference[DailyVoiceKeys.ADD_DATE] = todayStr
        }
    }

    suspend fun updateVoicesGroupedBy(voicesGroupedBy: VoicesGroupedBy) {
        voicesDataStore.edit { preference ->
            val jsonString = Json.encodeToString(VoicesGroupedBy.serializer(), voicesGroupedBy)
            preference[VoicesKeys.VOICES_GROUPED_BY] = jsonString
        }
    }
}