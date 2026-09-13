package prac.tanken.shigure.ui.subaci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.ui.getNotoFamilyByLocalesNonComposable
import prac.tanken.shigure.ui.subaci.core.ui.screen.ErrorScreen
import prac.tanken.shigure.ui.subaci.core.ui.screen.LargeLoadingIndefinitelyScreen
import prac.tanken.shigure.ui.subaci.core.ui.screen.LoadingIndefinitelyScreen
import prac.tanken.shigure.ui.subaci.navigation.AppNavHost

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val appViewModel: AppViewModel = hiltViewModel()
            val appState by appViewModel.state.collectAsStateWithLifecycle()
            val appCxt = LocalContext.current.applicationContext

            val lifecycleOwner = LocalLifecycleOwner.current
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(lifecycleOwner) {
                lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    appViewModel.effect.collect {
                        when (it) {
                            is AppContract.Effect.ShowSnackbar -> snackbarHostState.showSnackbar(it.message)
                        }
                    }
                }
            }

            when {
                appState.appSettingsState is AppContract.AppSettingsState.Loading
                        || appState.appResourceState is AppContract.AppResourceState.Loading -> {
                    LargeLoadingIndefinitelyScreen()
                }

                appState.appResourceState is AppContract.AppResourceState.Loaded
                        && appState.appSettingsState is AppContract.AppSettingsState.Loaded -> {
                    val appSettings =
                        (appState.appSettingsState as AppContract.AppSettingsState.Loaded).appSettings
                    val fontFamily =
                        (appState.appResourceState as AppContract.AppResourceState.Loaded).fontFamily

                    val subaciAssetsLoaded by appViewModel.subaciAssetsLoaded.collectAsStateWithLifecycle()

                    ShigureUiButtonAppComposeImplementationTheme(
                        appColor = appSettings.uiSettings.appColor,
                        appDarkMode = appSettings.uiSettings.appDarkMode,
                        fontFamily = fontFamily,
//                        fontFamily = FontFamily.SansSerif,
                        jpFont = appSettings.uiSettings.jpFont
                    ) {
                        if(subaciAssetsLoaded) {
                            AppNavHost(
                                navController = rememberNavController(),
                                appSettings = appSettings,
                            )
                        } else {
                            LoadingIndefinitelyScreen()
                        }
                    }
                }

                else -> {
                    val pagerState = rememberPagerState(0) { 2 }
                    HorizontalPager(
                        state = pagerState
                    ) { page ->
                        if (page == 0) {
                            val state =
                                appState.appResourceState as AppContract.AppResourceState.Error

                            ErrorScreen(
                                message = state.throwable.message,
                                stackTrace = state.throwable.stackTraceToString()
                            )
                        } else {
                            val state =
                                appState.appSettingsState as AppContract.AppSettingsState.Error

                            ErrorScreen(
                                message = state.throwable.message,
                                stackTrace = state.throwable.stackTraceToString()
                            )
                        }
                    }
                }
            }
        }
    }
}