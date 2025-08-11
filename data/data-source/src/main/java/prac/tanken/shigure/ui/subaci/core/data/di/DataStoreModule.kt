package prac.tanken.shigure.ui.subaci.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

import prac.tanken.shigure.ui.subaci.core.data.preferences.dailyVoiceDataStore
import prac.tanken.shigure.ui.subaci.core.data.preferences.settingsDataStore
import prac.tanken.shigure.ui.subaci.core.data.preferences.voicesDataStore

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {
    @DailyVoiceDataStore
    @Singleton
    @Provides
    fun provideDailyVoiceDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> = appContext.dailyVoiceDataStore

    @SettingsDataStore
    @Singleton
    @Provides
    fun provideSettingsDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> = appContext.settingsDataStore

    @VoicesDataStore
    @Singleton
    @Provides
    fun provideVoicesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> = appContext.voicesDataStore
}