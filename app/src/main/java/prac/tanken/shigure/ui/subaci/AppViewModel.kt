package prac.tanken.shigure.ui.subaci

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.onEach
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

    init {
        viewModelScope.launch {
            appSettingsFlow.collect { appSettings -> appSettings?.let { _appSettings.value = it } }
        }
    }
}