package prac.tanken.shigure.ui.subaci.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SerializationModule {
    @VoicesGroupedByJson
    @Singleton
    @Provides
    fun provideVoicesGroupedByJsonInstance() = Json {
        serializersModule = SerializersModule {
            polymorphic(VoicesGroupedBy::class) {
                subclass(VoicesGroupedBy.Category::class, VoicesGroupedBy.Category.serializer())
                subclass(VoicesGroupedBy.Kana::class, VoicesGroupedBy.Kana.serializer())
            }
        }
    }
}