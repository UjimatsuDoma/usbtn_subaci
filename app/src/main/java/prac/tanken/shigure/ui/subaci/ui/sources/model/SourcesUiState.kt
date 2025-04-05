package prac.tanken.shigure.ui.subaci.ui.sources.model

import androidx.annotation.StringRes
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.R as TankenR

@Serializable
sealed interface SourcesUiState {
    @Serializable
    data object StandBy : SourcesUiState

    @Serializable
    data object Loading : SourcesUiState

    @Serializable
    data class Loaded(val sources: List<SourcesListItem>) : SourcesUiState {
        @Serializable
        data class Tab(
            @StringRes val tabName: Int,
            val sourceList: List<SourcesListItem>,
        )

        val tabs = listOf(
            Tab(
                tabName = TankenR.string.sources_tab_with_voices,
                sourceList = sources.filter { it.voices.isNotEmpty() }
            ),
            Tab(
                tabName = TankenR.string.sources_tab_without_voices,
                sourceList = sources.filter { it.voices.isEmpty() }
            ),
        )
    }

    @Serializable
    data class Error(val message: String) : SourcesUiState
}