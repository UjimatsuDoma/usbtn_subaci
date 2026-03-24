package prac.tanken.shigure.ui.subaci.fontman.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface FontDecompressState {
    @Serializable
    data object Intro : FontDecompressState
    @Serializable
    data class Progress(val percentage: Float) : FontDecompressState
    @Serializable
    data object Complete : FontDecompressState
    @Serializable
    data object Failed : FontDecompressState
}