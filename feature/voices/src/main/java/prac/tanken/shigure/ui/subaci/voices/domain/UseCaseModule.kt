package prac.tanken.shigure.ui.subaci.voices.domain

import android.content.Context
import android.media.MediaPlayer
import android.os.Build
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.playlist.domain.PlaylistUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideDailyVoiceUseCase(
        voicesRepository: VoicesRepository,
        resRepository: ResRepository,
    ) = DailyVoiceUseCase(
        resRepository, voicesRepository
    )

    @Provides
    @Singleton
    fun provideVoiceUseCase(
        resRepository: ResRepository,
        voicesRepository: VoicesRepository,
    ) = VoicesUseCase(
        resRepository, voicesRepository
    )
}