package prac.tanken.shigure.ui.subaci.ui.voices

import prac.tanken.shigure.ui.subaci.data.model.Voice

sealed class DailyVoiceUiState {
    data object Loading : DailyVoiceUiState()
    data class Loaded(val voice: Voice) : DailyVoiceUiState()
    data object Error : DailyVoiceUiState()
}