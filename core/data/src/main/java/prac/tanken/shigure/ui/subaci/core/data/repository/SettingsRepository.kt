package prac.tanken.shigure.ui.subaci.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.core.data.di.SettingsDataStore
import prac.tanken.shigure.ui.subaci.core.data.preferences.SettingsKeys
import prac.tanken.shigure.ui.subaci.core.data.settings.AppSettings
import javax.inject.Inject

class SettingsRepository @Inject constructor(
    @param:SettingsDataStore val settingsDataStore: DataStore<Preferences>,
) {
    val appSettingsFlow: Flow<AppSettings?> = settingsDataStore.data
        .map { preferences ->
            val jsonString = preferences[SettingsKeys.appSettings]
            jsonString?.let { Json.decodeFromString(it) }
        }

    suspend fun updateAppSettings(newValue: AppSettings) {
        settingsDataStore.edit { preferences ->
            val jsonString = Json.encodeToString(newValue)
            preferences[SettingsKeys.appSettings] = jsonString
        }
    }
}