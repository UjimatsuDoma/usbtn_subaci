package prac.tanken.shigure.ui.subaci.core.data.settings

import kotlinx.serialization.Serializable
import prac.tanken.shigure.ui.subaci.core.data.settings.ui.UiSettings

@Serializable
data class AppSettings(
    val uiSettings: UiSettings = UiSettings(),
)
