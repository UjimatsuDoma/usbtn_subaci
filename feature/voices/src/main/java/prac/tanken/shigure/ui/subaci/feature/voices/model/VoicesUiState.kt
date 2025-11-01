package prac.tanken.shigure.ui.subaci.feature.voices.model

import androidx.compose.runtime.Stable

@Stable
data class VoicesUiState(
    val dailyVoiceUiState: DailyVoiceUiState,
    val voicesGroupedUiState: VoicesGroupedUiState,
)

val initialVoicesUiState = VoicesUiState(DailyVoiceUiState.StandBy, VoicesGroupedUiState.StandBy)
