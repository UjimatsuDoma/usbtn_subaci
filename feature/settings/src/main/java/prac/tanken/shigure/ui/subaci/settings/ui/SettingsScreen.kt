package prac.tanken.shigure.ui.subaci.settings.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import prac.tanken.shigure.ui.subaci.core.data.model.settings.AppSettings
import prac.tanken.shigure.ui.subaci.settings.SettingsViewModel
import prac.tanken.shigure.ui.subaci.settings.ui.items.aboutSettings
import prac.tanken.shigure.ui.subaci.settings.ui.items.uiSettings

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel,
) {
    val appSettings by viewModel.appSettings

    SettingsScreen(
        modifier,
        appSettings = appSettings,
        onUpdateAppSettings = viewModel::updateAppSettings
    )
}

@Composable
private fun SettingsScreen(
    modifier: Modifier = Modifier,
    appSettings: AppSettings = AppSettings(),
    onUpdateAppSettings: (AppSettings) -> Unit = {},
) = LazyColumn(modifier) {
    uiSettings(
        appSettings = appSettings,
        onUpdateAppSettings = onUpdateAppSettings
    )
    aboutSettings()
}