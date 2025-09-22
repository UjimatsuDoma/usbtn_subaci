package prac.tanken.shigure.ui.subaci.feature.sources.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import prac.tanken.shigure.ui.subaci.feature.sources.ui.SourcesScreen
import prac.tanken.shigure.ui.subaci.feature.sources.SourcesViewModel

fun NavGraphBuilder.sourcesScreen() = composable<Sources> {
    val viewmodel = hiltViewModel<SourcesViewModel>()

    SourcesScreen(viewModel = viewmodel)
}