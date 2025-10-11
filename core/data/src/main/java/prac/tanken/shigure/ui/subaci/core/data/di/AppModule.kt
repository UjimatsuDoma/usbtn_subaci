package prac.tanken.shigure.ui.subaci.core.data.di

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideResources(@ApplicationContext appContext: Context): Resources = appContext.resources

    @Provides
    @Singleton
    fun provideAssets(@ApplicationContext appContext: Context): AssetManager = appContext.assets
}