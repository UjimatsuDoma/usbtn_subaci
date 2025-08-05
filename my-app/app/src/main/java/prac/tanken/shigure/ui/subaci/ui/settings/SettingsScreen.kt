package prac.tanken.shigure.ui.subaci.ui.settings

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import prac.tanken.shigure.ui.subaci.ui.app.AppSettings
import prac.tanken.shigure.ui.subaci.ui.settings.items.aboutSettings
import prac.tanken.shigure.ui.subaci.ui.settings.items.uiSettings

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