package prac.tanken.shigure.ui.subaci.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import prac.tanken.shigure.ui.subaci.ui.voices.AllVoicesScreen
import prac.tanken.shigure.ui.subaci.ui.voices.VoicesViewModel
import prac.tanken.shigure.ui.subaci.ui.category.CategoryScreen
import prac.tanken.shigure.ui.subaci.ui.category.CategoryViewModel
import prac.tanken.shigure.ui.subaci.ui.playlist.PlaylistScreen
import prac.tanken.shigure.ui.subaci.ui.playlist.PlaylistViewModel
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
    data object AllVoices : MainDestinations(
        displayName = TankenR.string.home_voices,
        desc = TankenR.string.home_voices_desc,
        unselectedIcon = FluentR.drawable.ic_fluent_person_voice_24_regular,
        selectedIcon = FluentR.drawable.ic_fluent_person_voice_24_filled,
    )

    @Serializable
    data object CategoryVoices : MainDestinations(
        displayName = TankenR.string.home_category,
        desc = TankenR.string.home_category_desc,
        unselectedIcon = FluentR.drawable.ic_fluent_class_24_regular,
        selectedIcon = FluentR.drawable.ic_fluent_class_24_filled,
    )

    @Serializable
    data object Playlist : MainDestinations(
        displayName = TankenR.string.home_playlist,
        desc = TankenR.string.home_playlist_desc,
        unselectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_regular,
        selectedIcon = FluentR.drawable.ic_fluent_receipt_play_24_filled,
    )
}

val destinationClasses =
    MainDestinations::class.sealedSubclasses
val destinations =
    destinationClasses.map { it.objectInstance as MainDestinations }

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val voicesViewModel = hiltViewModel<VoicesViewModel>()
    val categoryViewModel = hiltViewModel<CategoryViewModel>()
    val playlistViewModel = hiltViewModel<PlaylistViewModel>()

    NavHost(
        navController = navController,
        startDestination = MainDestinations.AllVoices,
        modifier = modifier
    ) {
        composable<MainDestinations.AllVoices> {
            AllVoicesScreen(viewModel = voicesViewModel)
        }
        composable<MainDestinations.CategoryVoices> {
            CategoryScreen(viewModel = categoryViewModel)
        }
        composable<MainDestinations.Playlist> {
            PlaylistScreen(viewModel = playlistViewModel)
        }
    }
}

@Composable
fun MainNavigationBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
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

            NavigationBarItem(
                icon = { Icon(icon, stringResource(dest.desc)) },
                label = {
                    Text(
                        text = stringResource(dest.displayName),
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                selected = selected,
                onClick = {
                    navController.popBackStack()
                    navController.navigate(route = dest)
                }
            )
        }
    }
}

@Composable
fun MainNavigationRail(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier,
        windowInsets = WindowInsets(0)
    ) {
        val navBackStackEntry = navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry.value?.destination

        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
        ) {
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
                        Text(
                            text = stringResource(dest.displayName),
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
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
    }
}
