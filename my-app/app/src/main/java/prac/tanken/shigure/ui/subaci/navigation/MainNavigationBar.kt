package prac.tanken.shigure.ui.subaci.navigation

import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import prac.tanken.shigure.ui.subaci.core.data.model.settings.ui.BottomBarLabelBehaviour
import prac.tanken.shigure.ui.subaci.playlist.navigation.Playlist
import prac.tanken.shigure.ui.subaci.settings.navigation.Settings
import prac.tanken.shigure.ui.subaci.sources.navigation.Sources
import prac.tanken.shigure.ui.subaci.voices.navigation.Voices

val destinations = listOf(
    Voices, Sources, Playlist, Settings
)

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
