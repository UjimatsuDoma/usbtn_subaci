package prac.tanken.shigure.ui.subaci.voices.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import prac.tanken.shigure.ui.subaci.base.domain.BaseUseCase
import prac.tanken.shigure.ui.subaci.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.base.model.voice.VoicesGrouped
import prac.tanken.shigure.ui.subaci.base.model.voice.mutableVoiceGroups
import prac.tanken.shigure.ui.subaci.base.model.voice.toVoicesVO
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.voices.R

class VoicesUseCase(
    val resRepository: ResRepository,
    val voicesRepository: VoicesRepository,
) : BaseUseCase() {
    val voicesFlow = resRepository.voicesFlow
    val categoriesFlow = resRepository.categoriesFlow
    val voicesGroupedByFlow = voicesRepository.voicesGroupedByFlow

    val voicesGroupedEventFlow: Flow<UseCaseEvent> =
        combineTransform(
            voicesGroupedByFlow,
            voicesFlow,
            categoriesFlow
        ) { voicesGroupedBy, voices, categories ->
            emit(UseCaseEvent.Loading)
            val voicesSorted = voices.sortedBy { voice -> voice.k }
            if (voicesGroupedBy != null) {
                when (voicesGroupedBy) {
                    VoicesGroupedBy.Category -> {
                        val voicesGrouped = mutableVoiceGroups().apply {
                            categories.forEach { category ->
                                val idList = category.idList
                                val categoryVoices = voicesSorted
                                    .filter { voice -> voice.id in idList.map { it.id } }
                                    .toList()
                                this.put(category.className, categoryVoices)
                            }
                        }
                        emit(UseCaseEvent.Success(VoicesGrouped.ByCategory(voicesGrouped)))
                    }

                    VoicesGroupedBy.Kana -> {
                        val voicesGrouped = voicesSorted
                            .groupBy { it.a }
                            .mapValues { it.value }
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
                            }.toMap()
                        emit(UseCaseEvent.Success(VoicesGrouped.ByKana(voicesGrouped)))
                    }
                }
            } else {
                voicesRepository.updateVoicesGroupedBy(VoicesGroupedBy.Kana)
                val message = buildString {
                    append(resRepository.stringRes(R.string.voices_grouped_by_reset_prefix))
                    append(resRepository.stringRes(VoicesGroupedBy.Kana.displayName))
                }
                emit(UseCaseEvent.Info(message))
            }
        }

    suspend fun updateVoicesGroupedBy(newValue: VoicesGroupedBy) =
        voicesRepository.updateVoicesGroupedBy(newValue)
}