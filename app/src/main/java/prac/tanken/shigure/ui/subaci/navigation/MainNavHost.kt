package prac.tanken.shigure.ui.subaci.navigation

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import prac.tanken.shigure.ui.subaci.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.core.data.settings.AppSettings
import prac.tanken.shigure.ui.subaci.core.ui.getNotoFamilyByLocalesNonComposable
import prac.tanken.shigure.ui.subaci.core.ui.screen.LargeLoadingIndefinitelyScreen
import prac.tanken.shigure.ui.subaci.feature.playlist.navigation.playlistScreen
import prac.tanken.shigure.ui.subaci.feature.settings.navigation.settingsScreen
import prac.tanken.shigure.ui.subaci.feature.sources.navigation.sourcesScreen
import prac.tanken.shigure.ui.subaci.feature.voices.navigation.Voices
import prac.tanken.shigure.ui.subaci.feature.voices.navigation.voicesScreen

@Composable
fun MainNavHost(
    modifier: Modifier = Modifier,
    appSettingsState: AppSettings,
) {
    val navController = rememberNavController()
    var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

    LocalConfiguration.current.also { config ->
        LaunchedEffect(config) {
            snapshotFlow { config.orientation }
                .collect { orientation = it }
        }
    }

    Surface {
        when (orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
                Scaffold(
                    modifier = modifier,
                    bottomBar = {
                        MainNavigationBar(
                            navController = navController,
                            bottomBarLabelBehaviour =
                                appSettingsState.uiSettings.bottomBarLabelBehaviour,
                        )
                    }
                ) { innerPadding ->
                    MainNavHost(
                        navController = navController,
                        modifier = Modifier
                            .padding(innerPadding)
                            .consumeWindowInsets(innerPadding)
                    )
                }
            }

            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(modifier) {
                    MainNavHost(
                        navController,
                        modifier
                            .fillMaxHeight()
                            .weight(1f)
                    )
                    MainNavigationRail(
                        navController = navController,
                        bottomBarLabelBehaviour =
                            appSettingsState.uiSettings.bottomBarLabelBehaviour
                    )
                }
            }

            else -> {}
        }
    }
}

@Composable
private fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
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
