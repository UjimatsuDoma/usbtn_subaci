package prac.tanken.shigure.ui.subaci.core.domain.usecase.voices

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.transform
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped.*
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.core.domain.model.UseCaseEvent
import prac.tanken.shigure.ui.subaci.core.domain.model.UseCaseEvent.*
import prac.tanken.shigure.ui.subaci.core.ui.UiText
import javax.inject.Inject
import kotlin.to

class GetVoicesUseCase @Inject constructor(
    val voicesRepository: VoicesRepository,
) {
    operator fun invoke(
        voicesGroupedBy: VoicesGroupedBy,
    ): Flow<VoicesGrouped<*>> {
        val voices = voicesRepository.voicesMetadata
            ?: error("Voices are not loaded yet.")
        val categories = voicesRepository.categoriesMetadata
            ?: error("Categories are not loaded yet.")
        val sources = voicesRepository.sourcesMetadata
            ?: error("Voice sources are not loaded yet.")

        return flow {
            val voicesSorted = voices.sortedBy { it.k }
            when (voicesGroupedBy) {
                VoicesGroupedBy.Category -> {
                    val voicesGrouped = buildMap {
                        categories.forEach { category ->
                            val idList = category.idList
                            val categoryVoices = voicesSorted
                                .filter { voice -> voice.id in idList.map { it.id } }
                                .toList()
                            this[category.className] = categoryVoices
                        }
                    }
                    emit(ByCategory(voicesGrouped))
                }

                VoicesGroupedBy.Kana -> {
                    val voicesGrouped = voicesSorted
                        .groupBy { voice -> voice.a }
                        .mapKeys { entry ->
                            when (entry.key) {
                                "A" -> "あ行"
                                "KA" -> "か行"
                                "SA" -> "さ行"
                                "TA" -> "た行"
                                "NA" -> "な行"
                                "HA" -> "は行"
                                "MA" -> "ま行"
                                "YA" -> "や行"
                                "RA" -> "ら行"
                                "WA" -> "わ行"
                                else -> "その他"
                            }
                        }
                    emit(ByKana(voicesGrouped))
                }

                VoicesGroupedBy.None -> {
                    val voicesGrouped = mapOf(
                        Unit to voicesSorted
                    )
                    emit(ByNone(voicesGrouped))
                }

                VoicesGroupedBy.Video -> {
                    val voicesGrouped = voices
                        .filter { it.videoId != null }
                        .groupBy { it.videoId }
                        .map { voices ->
                            val source = sources.first { it.videoId == voices.key }
                            source to voices.value
                        }
                        .toMap()
                    emit(ByVideo(voicesGrouped))
                }
            }
        }
    }
}