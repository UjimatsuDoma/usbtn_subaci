package prac.tanken.shigure.ui.subaci.core.data.di

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import prac.tanken.shigure.ui.subaci.core.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.core.data.datasource.AssetsDataSource
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.preferences.dailyVoiceDataStore
import prac.tanken.shigure.ui.subaci.core.data.preferences.settingsDataStore
import prac.tanken.shigure.ui.subaci.core.data.preferences.voicesDataStore
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.SettingsRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    // data source
    @Singleton
    @Provides
    fun providePlaylistDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(
        appContext,
        PlaylistDatabase::class.java,
        "playlists.db"
    ).build()

    @Singleton
    @Provides
    fun provideAssetsDataSource(
        @ApplicationContext applicationContext: Context
    ) = AssetsDataSource(applicationContext)

    // repo
    // infra repo
    @Singleton
    @Provides
    fun provideResRepository(
        res: Resources,
        am: AssetManager,
    ) = ResRepository(res, am)

    // feature repo
    @Singleton
    @Provides
    fun provideVoicesRepository(
        @VoicesGroupedByJson json: Json,
        @VoicesDataStore voicesDataStore: DataStore<Preferences>,
        @DailyVoiceDataStore dailyVoiceDataStore: DataStore<Preferences>,
        assetsDataSource: AssetsDataSource
    ) = VoicesRepository(
        json,
        voicesDataStore,
        dailyVoiceDataStore,
        assetsDataSource
    )

    @Singleton
    @Provides
    fun providePlaylistRepository(
        playlistDatabase: PlaylistDatabase,
    ) = PlaylistRepository(playlistDatabase)

    @Singleton
    @Provides
    fun provideSettingsRepository(
        @SettingsDataStore dataStore: DataStore<Preferences>
    ) = SettingsRepository(dataStore)

    @VoicesGroupedByJson
    @Singleton
    @Provides
    fun provideVoicesGroupedByJsonInstance() = Json {
        serializersModule = SerializersModule {
            polymorphic(VoicesGroupedBy::class) {
                subclass(VoicesGroupedBy.Category::class, VoicesGroupedBy.Category.serializer())
                subclass(VoicesGroupedBy.Kana::class, VoicesGroupedBy.Kana.serializer())
                subclass(VoicesGroupedBy.None::class, VoicesGroupedBy.None.serializer())
            }
        }
    }

    @VoicesDataStore
    @Singleton
    @Provides
    fun provideVoicesDataStore(
        @ApplicationContext appContext: Context
    ): DataStore<Preferences> = appContext.voicesDataStore

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
}