package prac.tanken.shigure.ui.subaci.feature.voices.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import prac.tanken.shigure.ui.subaci.feature.voices.R
import prac.tanken.shigure.ui.subaci.core.common.datetime.todayStr
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.feature.base.domain.BaseUseCase
import prac.tanken.shigure.ui.subaci.feature.base.domain.UseCaseEvent

class DailyVoiceUseCase(
    val resRepository: ResRepository,
    val voicesRepository: VoicesRepository,
): BaseUseCase() {
    val voicesFlow = resRepository.voicesFlow
    val dailyVoiceEntityFlow = voicesRepository.dailyVoiceEntityFlow

    val dailyVoiceEventFlow: Flow<UseCaseEvent> =
        combineTransform(dailyVoiceEntityFlow, voicesFlow) { dailyVoiceEntity, voices ->
            val expired = dailyVoiceEntity?.addDate?.let { todayStr != it } == true
            if (dailyVoiceEntity == null || expired) {
                voicesRepository.updateDailyVoice(voices.random().id)
                emit(UseCaseEvent.Info(resRepository.stringRes(R.string.daily_random_voice_refreshed)))
            } else {
                val voice = voices.first { it.id == dailyVoiceEntity.voiceId }
                emit(UseCaseEvent.Success(voice))
            }
        }
}