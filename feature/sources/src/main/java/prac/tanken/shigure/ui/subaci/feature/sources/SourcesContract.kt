package prac.tanken.shigure.ui.subaci.feature.sources

import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiEffect
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiIntent
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiState

object SourcesContract {
    data class State(
        val sourcesUiState: SourcesUiState = SourcesUiState.Loading
    ) : UiState

    sealed interface SourcesUiState {
        data object Loading : SourcesUiState
        data class Loaded(
            val sourcesWithVoice: VoicesGrouped.ByVideo,
            val sourcesWithoutVoice: List<SourceEntity>,
        ) : SourcesUiState
        data class Error(val message: String) : SourcesUiState {
            companion object {
                fun fromThrowable(throwable: Throwable) =
                    Error(throwable.message ?: throwable.javaClass.simpleName)
            }
        }
    }

    sealed interface Intent : UiIntent {
        data class PlayVoice(val voice: Voice) : Intent
    }

    sealed interface Effect : UiEffect
}