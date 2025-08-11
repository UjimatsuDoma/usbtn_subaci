package prac.tanken.shigure.ui.subaci.core.data.di

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import prac.tanken.shigure.ui.subaci.core.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Singleton
    @Provides
    fun providePlaylistDatabase(
        @ApplicationContext appContext: Context
    ) = Room.databaseBuilder(
        appContext,
        PlaylistDatabase::class.java,
        "playlists.db"
    ).build()
}