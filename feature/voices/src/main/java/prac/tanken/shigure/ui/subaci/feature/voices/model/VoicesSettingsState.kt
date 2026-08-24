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

    @Serializable
    data class Error(
        val message: String
    ): VoicesSettingsState {
        companion object {
            fun fromThrowable(throwable: Throwable)  =
                Error(throwable.message ?: throwable.javaClass.simpleName)
        }
    }
}