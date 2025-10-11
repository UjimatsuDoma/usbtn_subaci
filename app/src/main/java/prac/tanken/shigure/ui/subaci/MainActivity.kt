package prac.tanken.shigure.ui.subaci

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import prac.tanken.shigure.ui.subaci.core.ui.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.navigation.MainNavHost
import prac.tanken.shigure.ui.subaci.navigation.MainNavigationBar
import prac.tanken.shigure.ui.subaci.navigation.MainNavigationRail

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val appViewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                appViewModel.resourcesLoaded.value && appViewModel.settingsLoaded.value
            }
        }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val appViewModel: AppViewModel = viewModel()

            val appSettingsState by appViewModel.appSettings

            ShigureUiButtonAppComposeImplementationTheme(
                appSettingsState.uiSettings.appColor,
                appSettingsState.uiSettings.appDarkMode
            ) {
                val navController = rememberNavController()
                var orientation by remember { mutableIntStateOf(Configuration.ORIENTATION_PORTRAIT) }

                LocalConfiguration.current.also { config ->
                    LaunchedEffect(config) {
                        snapshotFlow { config.orientation }
                            .collect { orientation = it }
                    }
                }

                when (orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        Scaffold(
                            modifier = Modifier.Companion.fillMaxSize(),
                            bottomBar = {
                                MainNavigationBar(
                                    navController = navController,
                                    bottomBarLabelBehaviour =
                                        appSettingsState.uiSettings.bottomBarLabelBehaviour
                                )
                            }
                        ) { innerPadding ->
                            MainNavHost(
                                navController,
                                Modifier.Companion
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                    }

                    Configuration.ORIENTATION_LANDSCAPE -> {
                        Scaffold { innerPadding ->
                            Row(Modifier.Companion.padding(innerPadding)) {
                                MainNavHost(
                                    navController,
                                    Modifier.Companion
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
                    }

                    else -> {}
                }
            }
        }
    }
}