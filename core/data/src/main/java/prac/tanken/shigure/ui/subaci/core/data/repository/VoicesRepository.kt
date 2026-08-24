package prac.tanken.shigure.ui.subaci.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.core.common.datetime.todayStr
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.datasource.AssetsDataSource
import prac.tanken.shigure.ui.subaci.core.data.datasource.ResDataSource
import prac.tanken.shigure.ui.subaci.core.data.di.DailyVoiceDataStore
import prac.tanken.shigure.ui.subaci.core.data.di.VoicesDataStore
import prac.tanken.shigure.ui.subaci.core.data.di.VoicesGroupedByJson
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.core.data.model.voices.Category
import prac.tanken.shigure.ui.subaci.core.data.model.voices.DailyVoiceEntity
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.preferences.DailyVoiceKeys
import prac.tanken.shigure.ui.subaci.core.data.preferences.VoicesKeys
import javax.inject.Inject
import kotlin.concurrent.Volatile

/**
 * keeps data read from res
 */
class VoicesRepository(
    @VoicesGroupedByJson val voicesGroupedByJson: Json,
    @VoicesDataStore val voicesDataStore: DataStore<Preferences>,
    @DailyVoiceDataStore val dailyVoiceDataStore: DataStore<Preferences>,
    val assetsDataSource: AssetsDataSource,
) {
    @Volatile
    var voicesMetadata: List<Voice>? = null
        private set
    var categoriesMetadata: List<Category>? = null
        private set
    var sourcesMetadata: List<SourceEntity>?=null
        private set

    /**
     * useful for initializing voices data when app opens up
     */
    fun loadVoices(): Flow<RepositoryEvent<Unit, Exception>> {
        return flow {
            emit(RepositoryEvent.Working)
            try {
                val voicesJson = assetsDataSource openFileAsString VOICES_JSON
                // handle properties misspelled by original website author
                val voices: List<Voice> = parseJsonString<MutableList<Voice>>(voicesJson).also {
                    it.forEachIndexed { index, voice ->
                        if (voice.a == "AS" || voice.a == "ZA") it[index] = voice.copy(a = "SA")
                    }
                }
                voicesMetadata = voices
                emit(RepositoryEvent.Success(Unit))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(RepositoryEvent.Error(e))
            }
        }
    }

    fun loadCategories(): Flow<RepositoryEvent<Unit, Exception>> {
        return flow {
            emit(RepositoryEvent.Working)
            try {
                val categoriesJson = assetsDataSource openFileAsString CATEGORIES_JSON
                val categories: List<Category> = parseJsonString(categoriesJson)
                categoriesMetadata = categories
                emit(RepositoryEvent.Success(Unit))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(RepositoryEvent.Error(e))
            }
        }
    }

    fun loadSources(): Flow<RepositoryEvent<Unit, Exception>> {
        return flow {
            emit(RepositoryEvent.Working)
            try {
                val sourcesJson = assetsDataSource openFileAsString SOURCES_JSON
                val sources: List<SourceEntity> = parseJsonString(sourcesJson)
                sourcesMetadata = sources
                emit(RepositoryEvent.Success(Unit))
            } catch (e: Exception) {
                e.printStackTrace()
                emit(RepositoryEvent.Error(e))
            }
        }
    }

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