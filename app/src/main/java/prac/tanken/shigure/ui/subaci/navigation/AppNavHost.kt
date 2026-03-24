package prac.tanken.shigure.ui.subaci.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.settings.AppSettings
import prac.tanken.shigure.ui.subaci.fontman.ui.FontManagementNavHost

@Serializable
data object Main

@Serializable
data object FontManagement

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    appSettings: AppSettings,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = FontManagement
    ) {
        composable<Main> {
            MainNavHost(appSettingsState = appSettings)
        }
        composable<FontManagement> {
            FontManagementNavHost(
                onPassed = { navController.navigate(Main) }
            )
        }
    }
}