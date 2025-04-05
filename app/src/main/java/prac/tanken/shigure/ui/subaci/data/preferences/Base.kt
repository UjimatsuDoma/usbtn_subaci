package prac.tanken.shigure.ui.subaci.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dailyVoiceDataStore: DataStore<Preferences> by preferencesDataStore("subaci_daily_voice")
val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore("subaci_settings")
val Context.voicesDataStore: DataStore<Preferences> by preferencesDataStore("subaci_voices")