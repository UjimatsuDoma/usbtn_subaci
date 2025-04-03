package prac.tanken.shigure.ui.subaci.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.data.model.voices.mutableVoiceGroups
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.ui.voices.model.toVoicesVO

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
            voicesGroupedBy?.let {
                when (it) {
                    VoicesGroupedBy.Category -> {
                        val voicesGrouped = mutableVoiceGroups().apply {
                            categories.forEach { category ->
                                val categoryVoices = category.idList.map { voiceRef ->
                                    voices
                                        .map { it.toVoicesVO() }
                                        .first { it.id == voiceRef.id }
                                }.toList()
                                this.put(category.className, categoryVoices)
                            }
                        }
                        emit(UseCaseEvent.Success(VoicesGrouped.ByCategory(voicesGrouped)))
                    }

                    VoicesGroupedBy.Kana -> {
                        val voicesGrouped = voices
                            .map { it.toVoicesVO() }
                            .groupBy { it.a }
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
            } ?: suspend {
                voicesRepository.updateVoicesGroupedBy(VoicesGroupedBy.Kana)
                val message = buildString {
                    append(resRepository.stringRes(R.string.voices_grouped_by_reset_prefix))
                    append(resRepository.stringRes(R.string.voices_grouped_by_kana))
                }
                emit(UseCaseEvent.Info(message))
            }.invoke()
        }

    suspend fun updateVoicesGroupedBy(newValue: VoicesGroupedBy) =
        voicesRepository.updateVoicesGroupedBy(newValue)
}