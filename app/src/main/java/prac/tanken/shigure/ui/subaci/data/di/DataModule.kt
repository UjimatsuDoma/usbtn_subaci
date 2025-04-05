package prac.tanken.shigure.ui.subaci.data.di

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
import prac.tanken.shigure.ui.subaci.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.data.preferences.dailyVoiceDataStore
import prac.tanken.shigure.ui.subaci.data.preferences.settingsDataStore
import prac.tanken.shigure.ui.subaci.data.preferences.voicesDataStore
import prac.tanken.shigure.ui.subaci.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.repository.SettingsRepository
import prac.tanken.shigure.ui.subaci.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.domain.DailyVoiceUseCase
import prac.tanken.shigure.ui.subaci.domain.SourcesUseCase
import prac.tanken.shigure.ui.subaci.domain.VoicesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
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
    fun providePlaylistRepository(
        playlistDatabase: PlaylistDatabase,
    ) = PlaylistRepository(playlistDatabase)

    @Singleton
    @Provides
    fun provideDailyVoiceUseCase(
        resRepository: ResRepository,
        voicesRepository: VoicesRepository,
    ) = DailyVoiceUseCase(resRepository, voicesRepository)

    @Singleton
    @Provides
    fun provideVoicesUseCase(
        resRepository: ResRepository,
        voicesRepository: VoicesRepository,
    ) = VoicesUseCase(resRepository, voicesRepository)

    @Singleton
    @Provides
    fun provideSourcesUseCase(
        resRepository: ResRepository,
    ) = SourcesUseCase(resRepository)

    @Singleton
    @Provides
    fun providePlaylistUseCase(
        playlistRepository: PlaylistRepository,
        resRepository: ResRepository,
    ) = PlaylistUseCase(playlistRepository, resRepository)

    @Singleton
    @Provides
    fun provideResRepository(
        res: Resources,
        am: AssetManager,
    ) = ResRepository(res, am)

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