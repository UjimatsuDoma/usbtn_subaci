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
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShigureUiButtonAppComposeImplementationTheme {
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
                            bottomBar = { MainNavigationBar(navController) }
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
                                MainNavigationRail(navController)
                            }
                        }
                    }

                    Configuration.ORIENTATION_SQUARE -> {}
                    Configuration.ORIENTATION_UNDEFINED -> {}
                }
            }
        }
    }
}