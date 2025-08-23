package prac.tanken.shigure.ui.subaci.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import prac.tanken.shigure.ui.subaci.playlist.navigation.playlistScreen
import prac.tanken.shigure.ui.subaci.settings.navigation.settingsScreen
import prac.tanken.shigure.ui.subaci.sources.navigation.sourcesScreen
import prac.tanken.shigure.ui.subaci.voices.navigation.Voices
import prac.tanken.shigure.ui.subaci.voices.navigation.voicesScreen

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val saveableStateHolder = rememberSaveableStateHolder()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    NavHost(
        navController = navController,
        startDestination = Voices,
        modifier = modifier
    ) {
        voicesScreen()
        sourcesScreen()
        playlistScreen()
        settingsScreen()
    }
}