package prac.tanken.shigure.ui.subaci.data.di

import javax.inject.Qualifier

// DataStore
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DailyVoiceDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoicesDataStore

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SettingsDataStore

// Json
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class VoicesGroupedByJson