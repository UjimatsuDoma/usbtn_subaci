package prac.tanken.shigure.ui.subaci.data.di

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
    fun provideMediaPlayer(@ApplicationContext appContext: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            MediaPlayer(appContext)
        } else {
            MediaPlayer()
        }
}