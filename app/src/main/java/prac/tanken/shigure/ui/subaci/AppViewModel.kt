package prac.tanken.shigure.ui.subaci

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.repository.RepositoryEvent
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.SettingsRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.core.data.settings.AppSettings
import prac.tanken.shigure.ui.subaci.feature.base.SnackbarMessage
import prac.tanken.shigure.ui.subaci.feature.base.mvi.BaseViewModel
import prac.tanken.shigure.ui.subaci.feature.settings.R
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val settingsRepository: SettingsRepository,
    val resRepository: ResRepository,
    val voicesRepository: VoicesRepository,
) : BaseViewModel<AppContract.State, AppContract.Intent, AppContract.Effect>() {
    override fun initState(): AppContract.State = AppContract.State()

    override fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            observeAppSettings()
        }
        viewModelScope.launch(Dispatchers.IO) {
            loadFontFamily()
        }
    }

    private suspend fun observeAppSettings() =
        settingsRepository.appSettingsFlow
            .distinctUntilChanged()
            .onEach { settings ->
                settings?.let {
                    setState {
                        copy(
                            appSettingsState = AppContract.AppSettingsState.Loaded(it)
                        )
                    }
                } ?: run {
                    settingsRepository.updateAppSettings(AppSettings())
                    sendEffect(
                        AppContract.Effect.ShowSnackbar(
                            resRepository.stringRes(R.string.settings_initialized_message)
                        )
                    )
                }
            }
            .catch {
                setState {
                    copy(
                        appSettingsState = AppContract.AppSettingsState.Error(it)
                    )
                }
            }
            .collect()

    private suspend fun loadFontFamily(): Nothing =
        state.collect { currentState ->
            val appSettingsState = currentState.appSettingsState
            if (appSettingsState is AppContract.AppSettingsState.Loaded) {
                val notoStyle = appSettingsState.appSettings.uiSettings.notoStyle
                setState {
                    copy(
                        appResourceState = AppContract.AppResourceState.Loaded(
                            fontFamily = resRepository.getVariableFontFamily(notoStyle)
                        )
                    )
                }
            }
        }

    val subaciAssetsLoaded = voicesRepository.run {
        val f1 = loadVoices().map { it is RepositoryEvent.Success }
        val f2 = loadCategories().map { it is RepositoryEvent.Success }
        val f3 = loadSources().map { it is RepositoryEvent.Success }
        combineTransform(f1, f2, f3) { a, b, c ->
            emit(a && b && c)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
}