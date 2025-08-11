package prac.tanken.shigure.ui.subaci.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.core.data.model.settings.ui.BottomBarLabelBehaviour
import prac.tanken.shigure.ui.subaci.ui.playlist.PlaylistScreen
import prac.tanken.shigure.ui.subaci.ui.playlist.PlaylistViewModel
import prac.tanken.shigure.ui.subaci.ui.settings.SettingsScreen
import prac.tanken.shigure.ui.subaci.ui.settings.SettingsViewModel
import prac.tanken.shigure.ui.subaci.ui.sources.SourcesScreen
import prac.tanken.shigure.ui.subaci.ui.sources.SourcesViewModel
import prac.tanken.shigure.ui.subaci.ui.voices.VoicesScreen
import prac.tanken.shigure.ui.subaci.ui.voices.VoicesViewModel
import com.microsoft.fluent.mobile.icons.R as FluentR
import prac.tanken.shigure.ui.subaci.R as TankenR

@Serializable
sealed class MainDestinations(
    @StringRes val displayName: Int,
    @StringRes val desc: Int,
    @DrawableRes val unselectedIcon: Int,
    @DrawableRes val selectedIcon: Int,
) {
    @Serializable
    data object Voices : MainDestinations(
        displayName = TankenR.string.home_voices,
        desc = TankenR.string.home_voices_desc,
        unselectedIcon = FluentR.drawable.ic_fluent_person_voice_24_regular,
        selectedIcon = FluentR.drawable.ic_fluent_person_voice_24_filled,
    )

    @Serializable
    data object Sources : MainDestinations(
        displayName = TankenR.string.home_sources,
        desc = TankenR.string.home_sources_desc,
        unselectedIcon = FluentR.drawable.ic_fluent_video_clip_24_regular,
        selectedIcon = FluentR.drawable.ic_fluent_video_clip_24_filled,
    )

    @Serializable
    data object Playlist : MainDestinations(
        displayName = TankenR.string.home_playlist,
        desc = TankenR.string.home_playlist_desc,
        unselectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_regular,
        selectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_filled,
    )

    @Serializable
    data object Settings : MainDestinations(
        displayName = TankenR.string.home_settings,
        desc = TankenR.string.home_settings_desc,
        unselectedIcon = FluentR.drawable.ic_fluent_settings_24_regular,
        selectedIcon = FluentR.drawable.ic_fluent_settings_24_filled,
    )
}

val destinations = listOf(
    MainDestinations.Voices,
    MainDestinations.Sources,
    MainDestinations.Playlist,
    MainDestinations.Settings,
)

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val voicesViewModel = hiltViewModel<VoicesViewModel>()
    val sourcesViewModel = hiltViewModel<SourcesViewModel>()
    val playlistViewModel = hiltViewModel<PlaylistViewModel>()
    val settingsViewModel = hiltViewModel<SettingsViewModel>()

    val saveableStateHolder = rememberSaveableStateHolder()
    val backStackEntry = navController.currentBackStackEntryAsState().value
    val currentRoute = backStackEntry?.destination?.route

    saveableStateHolder.SaveableStateProvider(
        key = currentRoute ?: ""
    ) {
        NavHost(
            navController = navController,
            startDestination = MainDestinations.Voices,
            modifier = modifier
        ) {
            composable<MainDestinations.Voices> {
                VoicesScreen(viewModel = voicesViewModel)
            }
            composable<MainDestinations.Sources> {
                SourcesScreen(viewModel = sourcesViewModel)
            }
            composable<MainDestinations.Playlist> {
                PlaylistScreen(viewModel = playlistViewModel)
            }
            composable<MainDestinations.Settings> {
                SettingsScreen(viewModel = settingsViewModel)
            }
        }
    }


}

@Composable
fun MainNavigationBar(
    navController: NavController,
    bottomBarLabelBehaviour: BottomBarLabelBehaviour,
    modifier: Modifier = Modifier
) = NavigationBar(modifier) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    destinations.forEachIndexed { index, dest ->
        val selected = currentDestination?.route == dest.javaClass.canonicalName
        val icon = if (selected) {
            painterResource(dest.selectedIcon)
        } else {
            painterResource(dest.unselectedIcon)
        }

        NavigationBarItem(
            icon = { Icon(icon, stringResource(dest.desc)) },
            label = {
                if (bottomBarLabelBehaviour.showLabel(selected)) {
                    Text(
                        text = stringResource(dest.displayName),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            },
            selected = selected,
            onClick = {
                navController.navigate(route = dest) {
                    popUpTo(route = currentDestination?.route!!) {
                        inclusive = true
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

@Composable
fun MainNavigationRail(
    navController: NavController,
    bottomBarLabelBehaviour: BottomBarLabelBehaviour,
    modifier: Modifier = Modifier
) = NavigationRail(
    modifier = modifier,
    windowInsets = WindowInsets(0)
) {
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    destinations.forEachIndexed { index, dest ->
        val selected = currentDestination?.route == dest.javaClass.canonicalName
        val icon = if (selected) {
            painterResource(dest.selectedIcon)
        } else {
            painterResource(dest.unselectedIcon)
        }

        NavigationRailItem(
            icon = { Icon(icon, stringResource(dest.desc)) },
            label = {
                if (bottomBarLabelBehaviour.showLabel(selected)) {
                    Text(
                        text = stringResource(dest.displayName),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            },
            selected = selected,
            onClick = {
                navController.popBackStack()
                navController.navigate(route = dest) {
                    popUpTo(route = dest) {
                        inclusive = false
                        saveState = true
                    }
                    restoreState = true
                }
            },
        )
    }
}
