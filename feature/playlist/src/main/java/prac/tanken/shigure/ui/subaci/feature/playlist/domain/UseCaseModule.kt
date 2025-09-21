package prac.tanken.shigure.ui.subaci.feature.playlist.domain

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    @Singleton
    fun provideUseCase(
        playlistRepository: PlaylistRepository,
        resRepository: ResRepository,
    ) = PlaylistUseCase(
        playlistRepository, resRepository
    )
}