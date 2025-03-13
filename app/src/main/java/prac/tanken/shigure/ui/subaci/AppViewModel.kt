package prac.tanken.shigure.ui.subaci

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.repository.SettingsRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.ui.app.AppSettings
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
    private var _appSettings = mutableStateOf<AppSettings>(AppSettings())
    val appSettings = _appSettings

    val resourcesLoaded = resRepository.run {
        val voicesLoaded = voicesFlow.map { it.isNotEmpty() }
        val categoriesLoaded = categoriesFlow.map { it.isNotEmpty() }
        val sourcesLoaded = sourcesFlow.map { it.isNotEmpty() }
        voicesLoaded
            .combine(categoriesLoaded) { f1, f2 -> f1 && f2 }
            .combine(sourcesLoaded) { f1, f2 -> f1 && f2 }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, false)

    init {
        viewModelScope.launch {
            resRepository.apply {
                loadVoices()
                loadCategories()
                loadSources()
            }
            appSettingsFlow.collect { appSettings -> appSettings?.let { _appSettings.value = it } }
        }
    }
}