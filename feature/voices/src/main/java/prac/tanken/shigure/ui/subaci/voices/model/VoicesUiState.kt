package prac.tanken.shigure.ui.subaci.voices.model

import androidx.compose.runtime.Stable
import prac.tanken.shigure.ui.subaci.voices.model.daily.DailyVoiceUiState

@Stable
data class VoicesUiState(
    val dailyVoiceUiState: DailyVoiceUiState,
    val voicesGroupedUiState: VoicesGroupedUiState,
)

val initialVoicesUiState = VoicesUiState(DailyVoiceUiState.StandBy, VoicesGroupedUiState.StandBy)
