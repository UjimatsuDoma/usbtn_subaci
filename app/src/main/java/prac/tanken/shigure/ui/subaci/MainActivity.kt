package prac.tanken.shigure.ui.subaci

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import prac.tanken.shigure.ui.subaci.navigation.AppNavHost

@OptIn(ExperimentalMaterial3Api::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    sealed interface State {
        data object Loading : State
        data class Loaded(
            val fontFamily: FontFamily,
        ) : State

        data class Error(
            val throwable: Throwable
        ) : State
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val appViewModel: AppViewModel = hiltViewModel()
            val appSettingsState by appViewModel.appSettings
            val appCxt = LocalContext.current.applicationContext

            val scope = rememberCoroutineScope()
            var _state = MutableStateFlow<State>(State.Loading)
            val state = _state.collectAsStateWithLifecycle()
            with(appViewModel) {
                scope.launch {
                    combine(
                        settingsLoaded,
                        newResourcesLoaded
                    ) { f1, f2 ->
                        f1 && f2
                    }.collect {
                        if (it) {
                            val fontFamily = getNotoFamilyByLocalesNonComposable(
                                appCxt,
                                appSettingsState.uiSettings.notoStyle,
                            )
                            _state.update { State.Loaded(fontFamily) }
                        }
                    }
                }
            }

            val lifecycleOwner = LocalLifecycleOwner.current
            val snackbarHostState = remember { SnackbarHostState() }
            LaunchedEffect(lifecycleOwner) {
                lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    appViewModel.snackbarMessage.collect {
                        snackbarHostState.showSnackbar(it.message)
                    }
                }
            }

            when (val state = state.value) {
                State.Loading -> {
                    LargeLoadingIndefinitelyScreen()
                }

                is State.Error -> {
                    ErrorScreen(
                        message = state.throwable.message,
                        stackTrace = state.throwable.stackTraceToString()
                    )
                }

                is State.Loaded -> {
                    ShigureUiButtonAppComposeImplementationTheme(
                        appSettingsState.uiSettings.appColor,
                        appSettingsState.uiSettings.appDarkMode,
                        fontFamily = state.fontFamily,
                        jpFont = appSettingsState.uiSettings.jpFont
                    ) {
                        AppNavHost(
                            navController = rememberNavController(),
                            appSettings = appSettingsState,
                        )
                    }
                }
            }
        }
    }
}