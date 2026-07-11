package prac.tanken.shigure.ui.subaci.feature.base.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

abstract class BaseViewModel<S: UiState, I: UiIntent, E: UiEffect>: ViewModel() {
    abstract fun initState(): S

    protected val _state = MutableStateFlow(initState())
    val state = _state.asStateFlow()

    // 部分 ViewModel 功能不复杂，可以不实现Intent
    open fun sendIntent(intent: I) {}

    protected val _effect = Channel<E>()
    val effect = _effect.receiveAsFlow()
}