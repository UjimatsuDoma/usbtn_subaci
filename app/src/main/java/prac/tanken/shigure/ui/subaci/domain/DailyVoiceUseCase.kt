package prac.tanken.shigure.ui.subaci.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.helper.DailyVoiceHelper.todayStr
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.repository.VoicesRepository

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