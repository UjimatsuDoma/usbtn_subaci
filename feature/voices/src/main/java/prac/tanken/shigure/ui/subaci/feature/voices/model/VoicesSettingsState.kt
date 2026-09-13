package prac.tanken.shigure.ui.subaci.feature.voices.model

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy

@Serializable
sealed interface VoicesSettingsState {
    @Serializable
    data object Loading: VoicesSettingsState

    @Serializable
    data class Loaded(
        val voicesGroupedBy: VoicesGroupedBy,
    ): VoicesSettingsState

    data class Error(
        val throwable: Throwable
    ): VoicesSettingsState
}