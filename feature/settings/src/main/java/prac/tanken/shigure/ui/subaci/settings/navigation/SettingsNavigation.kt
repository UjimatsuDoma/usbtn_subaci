package prac.tanken.shigure.ui.subaci.settings.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import prac.tanken.shigure.ui.subaci.settings.ui.SettingsScreen
import prac.tanken.shigure.ui.subaci.settings.SettingsViewModel

fun NavGraphBuilder.settingsScreen() = composable<Settings> {
    val viewmodel = hiltViewModel<SettingsViewModel>()

    SettingsScreen(viewModel = viewmodel)
}