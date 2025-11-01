package prac.tanken.shigure.ui.subaci

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combineTransform
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.common.android.toast.ToastUtil
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.SettingsRepository
import prac.tanken.shigure.ui.subaci.core.data.settings.AppSettings
import prac.tanken.shigure.ui.subaci.feature.settings.R
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    val settingsRepository: SettingsRepository,
    val resRepository: ResRepository,
    val toastUtil: ToastUtil,
) : ViewModel() {
    private val appSettingsFlow = settingsRepository.appSettingsFlow
        .onEach { appSettings ->
            if (appSettings == null) {
                settingsRepository.updateAppSettings(AppSettings())
                toastUtil.toast(resRepository.stringRes(R.string.settings_initialized_message))
            }
        }
    private var _appSettings = mutableStateOf(AppSettings())
    val appSettings = _appSettings

    val resourcesLoaded = resRepository.run {
        val voicesLoaded = voicesFlow.map { it.isNotEmpty() }
        val categoriesLoaded = categoriesFlow.map { it.isNotEmpty() }
        val sourcesLoaded = sourcesFlow.map { it.isNotEmpty() }
        combineTransform(voicesLoaded, categoriesLoaded, sourcesLoaded) { f1, f2, f3 ->
            emit(f1 && f2 && f3)
        }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    private var _settingsLoaded = MutableStateFlow(false)
    val settingsLoaded = _settingsLoaded.asStateFlow()

    init {
        viewModelScope.launch {
            resRepository.apply {
                loadVoices()
                loadCategories()
                loadSources()
            }
        }
        viewModelScope.launch {
            appSettingsFlow.collect { appSettings ->
                appSettings?.let {
                    _appSettings.value = it
                    _settingsLoaded.value = true
                }
            }
        }
    }
}