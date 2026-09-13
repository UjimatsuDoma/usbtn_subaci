package prac.tanken.shigure.ui.subaci

import androidx.compose.runtime.Stable
import androidx.compose.ui.text.font.FontFamily
import prac.tanken.shigure.ui.subaci.core.data.settings.AppSettings
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiEffect
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiIntent
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiState

object AppContract {
    data class State(
        val appSettingsState: AppSettingsState = AppSettingsState.Loading,
        val appResourceState: AppResourceState = AppResourceState.Loading,
    ) : UiState

    sealed interface AppSettingsState {
        data object Loading : AppSettingsState
        data class Loaded(
            val appSettings: AppSettings,
        ) : AppSettingsState

        data class Error(
            val throwable: Throwable
        ) : AppSettingsState
    }

    sealed interface AppResourceState {
        data object Loading : AppResourceState
        @Stable
        data class Loaded(
            val fontFamily: FontFamily,
        ) : AppResourceState

        data class Error(
            val throwable: Throwable
        ) : AppResourceState
    }

    sealed interface Intent : UiIntent

    sealed interface Effect : UiEffect {
        data class ShowSnackbar(val message: String) : Effect
    }
}