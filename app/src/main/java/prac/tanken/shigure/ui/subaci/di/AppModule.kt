package prac.tanken.shigure.ui.subaci.di

import android.content.Context
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
    fun provideResources(@ApplicationContext appContext: Context) = appContext.resources

    @Provides
    @Singleton
    fun provideAssets(@ApplicationContext appContext: Context) = appContext.assets
}