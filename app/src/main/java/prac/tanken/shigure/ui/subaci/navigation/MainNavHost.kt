package prac.tanken.shigure.ui.subaci.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import prac.tanken.shigure.ui.subaci.feature.playlist.navigation.playlistScreen
import prac.tanken.shigure.ui.subaci.feature.settings.navigation.settingsScreen
import prac.tanken.shigure.ui.subaci.feature.sources.navigation.sourcesScreen
import prac.tanken.shigure.ui.subaci.feature.voices.navigation.Voices
import prac.tanken.shigure.ui.subaci.feature.voices.navigation.voicesScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    saveableStateHolder.SaveableStateProvider(
        key = currentRoute ?: ""
    ) {
        NavHost(
            navController = navController,
            startDestination = Voices,
            modifier = modifier
        ) {
            settingsScreen()
            playlistScreen()
            sourcesScreen()
            voicesScreen()
        }
    }
}
