package prac.tanken.shigure.ui.subaci.feature.voices.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import prac.tanken.shigure.ui.subaci.feature.voices.VoicesViewModel
import prac.tanken.shigure.ui.subaci.feature.voices.ui.VoicesScreen

fun NavGraphBuilder.voicesScreen() = composable<Voices> {
    val viewmodel = hiltViewModel<VoicesViewModel>()

    VoicesScreen(viewModel = viewmodel)
}