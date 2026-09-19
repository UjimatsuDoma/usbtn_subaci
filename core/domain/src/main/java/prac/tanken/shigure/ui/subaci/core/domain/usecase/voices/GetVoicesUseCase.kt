package prac.tanken.shigure.ui.subaci.core.domain.usecase.voices

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped.ByCategory
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped.ByKana
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped.ByNone
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped.ByVideo
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import javax.inject.Inject

class GetVoicesUseCase @Inject constructor(
    val voicesRepository: VoicesRepository,
) {
    operator fun invoke(
        voicesGroupedBy: VoicesGroupedBy,
    ): Flow<VoicesGrouped<*>> {
        val categories = voicesRepository.categoriesMetadata
            ?: error("Categories are not loaded yet.")
        val sources = voicesRepository.sourcesMetadata
            ?: error("Voice sources are not loaded yet.")

        return flow {
            val voicesSorted = voicesRepository.voicesMetadata.sortedBy { it.k }
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
                    val voicesGrouped = sources.associateWith { sourceEntity ->
                        voicesRepository.voicesMetadata.filter {
                            it.videoId == sourceEntity.videoId
                        }
                    }
                    emit(ByVideo(voicesGrouped))
                }
            }
        }
    }
}