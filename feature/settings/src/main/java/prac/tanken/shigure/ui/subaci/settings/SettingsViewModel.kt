package prac.tanken.shigure.ui.subaci.settings

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.model.settings.AppSettings
import prac.tanken.shigure.ui.subaci.core.data.repository.SettingsRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val settingsRepository: SettingsRepository,
) : ViewModel() {
    private val appSettingsFlow = settingsRepository.appSettingsFlow
    private var _appSettings = mutableStateOf(AppSettings())
    val appSettings = _appSettings

    init {
        viewModelScope.launch {
            appSettingsFlow.collect { appSettings -> appSettings?.let { _appSettings.value = it } }
        }
    }

    fun updateAppSettings(newValue: AppSettings) {
        viewModelScope.launch {
            settingsRepository.updateAppSettings(newValue)
        }
    }
}