package prac.tanken.shigure.ui.subaci.fontman.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface FontManagementDestinations {
    @Serializable
    data object Checking : FontManagementDestinations
    @Serializable
    data object Decompress : FontManagementDestinations
    @Serializable
    data object Instantiate : FontManagementDestinations
}