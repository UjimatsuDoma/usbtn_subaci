package prac.tanken.shigure.ui.subaci.feature.voices.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideVoicesUseCase(
        resRepository: ResRepository,
        voicesRepository: VoicesRepository
    ) = VoicesUseCase(resRepository, voicesRepository)

    @Provides
    @Singleton
    fun provideDailyVoiceUseCase(
        resRepository: ResRepository,
        voicesRepository: VoicesRepository
    ) = DailyVoiceUseCase(resRepository, voicesRepository)
}