package prac.tanken.shigure.ui.subaci.ui.voices.model

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGrouped

@Serializable
sealed interface VoicesGroupedUiState {
    @Serializable
    data object StandBy : VoicesGroupedUiState

    @Serializable
    data object Loading : VoicesGroupedUiState

    @Serializable
    data class Success(
        val voicesGrouped: VoicesGrouped
    ) : VoicesGroupedUiState

    @Serializable
    data object Error : VoicesGroupedUiState
}