package prac.tanken.shigure.ui.subaci.ui.voices.model

import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice


sealed interface DailyVoiceUiState {
    data object StandBy : DailyVoiceUiState
    data class Loaded(val voice: Voice) : DailyVoiceUiState
    data object Error : DailyVoiceUiState
}