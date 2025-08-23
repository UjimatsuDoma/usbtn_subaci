package prac.tanken.shigure.ui.subaci.voices.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import prac.tanken.shigure.ui.subaci.voices.ui.VoicesScreen
import prac.tanken.shigure.ui.subaci.voices.VoicesViewModel

fun NavGraphBuilder.voicesScreen() = composable<Voices> {
    val viewmodel = hiltViewModel<VoicesViewModel>()

    VoicesScreen(viewModel = viewmodel)
}