package prac.tanken.shigure.ui.subaci.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import prac.tanken.shigure.ui.subaci.core.data.settings.ui.NavigationLabelBehaviour

@Composable
fun MainNavigationRail(
    navController: NavController,
    bottomBarLabelBehaviour: NavigationLabelBehaviour,
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