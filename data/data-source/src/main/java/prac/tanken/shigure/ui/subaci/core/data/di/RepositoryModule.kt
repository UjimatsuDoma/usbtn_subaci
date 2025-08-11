package prac.tanken.shigure.ui.subaci.core.data.di

import android.content.res.AssetManager
import android.content.res.Resources
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import prac.tanken.shigure.ui.subaci.core.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.SettingsRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun providePlaylistRepository(
        playlistDatabase: PlaylistDatabase,
    ) = PlaylistRepository(playlistDatabase)

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
}