package prac.tanken.shigure.ui.subaci.fontman.domain

sealed interface FontInstantiateState {
    data object Intro : FontInstantiateState
    data class Progress(val percentage: Float) : FontInstantiateState
    data object Complete : FontInstantiateState
    data object Failed : FontInstantiateState
}