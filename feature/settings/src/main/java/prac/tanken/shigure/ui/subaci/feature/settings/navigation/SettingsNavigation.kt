package prac.tanken.shigure.ui.subaci.feature.settings.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import prac.tanken.shigure.ui.subaci.feature.settings.SettingsViewModel
import prac.tanken.shigure.ui.subaci.feature.settings.ui.SettingsScreen

fun NavGraphBuilder.settingsScreen() = composable<Settings> {
    val viewmodel = hiltViewModel<SettingsViewModel>()

    SettingsScreen(viewModel = viewmodel)
}