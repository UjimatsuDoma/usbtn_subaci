package prac.tanken.shigure.ui.subaci.data.di

import android.content.Context
import android.content.res.Resources
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import prac.tanken.shigure.ui.subaci.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun provideResRepository(
        res: Resources,
    ) = ResRepository(res)

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