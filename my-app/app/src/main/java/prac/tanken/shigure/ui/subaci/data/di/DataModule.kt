package prac.tanken.shigure.ui.subaci.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.domain.DailyVoiceUseCase
import prac.tanken.shigure.ui.subaci.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.domain.SourcesUseCase
import prac.tanken.shigure.ui.subaci.domain.VoicesUseCase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
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

//    @VoicesGroupedByJson
//    @Singleton
//    @Provides
//    fun provideVoicesGroupedByJsonInstance() = Json {
//        serializersModule = SerializersModule {
//            polymorphic(VoicesGroupedBy::class) {
//                subclass(VoicesGroupedBy.Category::class, VoicesGroupedBy.Category.serializer())
//                subclass(VoicesGroupedBy.Kana::class, VoicesGroupedBy.Kana.serializer())
//            }
//        }
//    }
}