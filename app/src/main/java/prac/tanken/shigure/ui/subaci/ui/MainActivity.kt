package prac.tanken.shigure.ui.subaci.ui

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import prac.tanken.shigure.ui.subaci.AppViewModel
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val appViewModel: AppViewModel = viewModel()

            val appSettingsState by appViewModel.appSettings

            ShigureUiButtonAppComposeImplementationTheme(
                appSettingsState.appColor, appSettingsState.appDarkMode
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
                            modifier = Modifier.fillMaxSize(),
                            bottomBar = {
                                MainNavigationBar(
                                    navController = navController,
                                    bottomBarLabelBehaviour = appSettingsState.bottomBarLabelBehaviour
                                )
                            }
                        ) { innerPadding ->
                            MainNavHost(
                                navController,
                                Modifier
                                    .fillMaxSize()
                                    .padding(innerPadding)
                            )
                        }
                    }

                    Configuration.ORIENTATION_LANDSCAPE -> {
                        Scaffold { innerPadding ->
                            Row(Modifier.padding(innerPadding)) {
                                MainNavHost(
                                    navController,
                                    Modifier
                                        .fillMaxHeight()
                                        .weight(1f)
                                )
                                MainNavigationRail(
                                    navController = navController,
                                    bottomBarLabelBehaviour = appSettingsState.bottomBarLabelBehaviour
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