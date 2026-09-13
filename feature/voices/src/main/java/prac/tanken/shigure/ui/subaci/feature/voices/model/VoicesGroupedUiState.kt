package prac.tanken.shigure.ui.subaci.feature.voices.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiError

@Serializable
sealed interface VoicesGroupedUiState {
    @Serializable
    data object StandBy : VoicesGroupedUiState

    @Serializable
    data object Loading : VoicesGroupedUiState

    @Serializable
    data class Success(
        val voicesGroups: Map<String, List<Voice>>
    ) : VoicesGroupedUiState

    @Serializable
    data class Error(
        override val message: String?,
        override val stackTrace: String,
    ) : VoicesGroupedUiState, UiError(message, stackTrace)
}