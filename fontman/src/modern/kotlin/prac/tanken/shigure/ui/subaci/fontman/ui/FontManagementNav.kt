package prac.tanken.shigure.ui.subaci.fontman.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import prac.tanken.shigure.ui.subaci.fontman.navigation.FontManagementDestinations

@Composable
fun FontManagementNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    onPassed: () -> Unit,
) {
    val vm: FontManagementViewModel = hiltViewModel()

    MaterialTheme {
        Surface {
            NavHost(navController, startDestination = FontManagementDestinations.Checking) {
                composable<FontManagementDestinations.Checking> {
                    FontCheckingScreen(
                        modifier,
                        vm,
                        onPassed = onPassed,
                        toDecompress = { navController.navigate(FontManagementDestinations.Decompress) },
                        toInstantiate = {}
                    )
                }
                composable<FontManagementDestinations.Decompress> {
                    FontDecompressingScreen(
                        modifier,
                        viewModel = vm,
                        onPassed = { navController.navigate(FontManagementDestinations.Checking) },
                        onBack = { navController.popBackStack() }
                    )
                }
            }
        }
    }
}