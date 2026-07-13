package prac.tanken.shigure.ui.subaci.feature.voices

import androidx.compose.runtime.Stable
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiEffect
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiIntent
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiState

object VoicesContract {
    @Stable
    data class State(
        val dailyVoiceUiState: DailyVoiceUiState = DailyVoiceUiState.StandBy,
        val voicesGroupedUiState: VoicesGroupedUiState = VoicesGroupedUiState.StandBy,
    ) : UiState

    sealed interface Intent : UiIntent {
        data class PlayVoice(val voice: Voice) : Intent
        data object PlayDailyVoice : Intent
        data class ChangeVoicesGroupedBy(val newValue: VoicesGroupedBy) : Intent
    }

    sealed interface Effect : UiEffect {
        data class ShowDailyVoiceToast(val voice: Voice) : Effect
        data class ShowToast(val message: String) : Effect
    }
}