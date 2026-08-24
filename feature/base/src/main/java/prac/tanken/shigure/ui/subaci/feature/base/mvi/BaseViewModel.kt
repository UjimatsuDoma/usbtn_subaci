package prac.tanken.shigure.ui.subaci.feature.base.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect> : ViewModel() {
    abstract fun initState(): S
    abstract fun loadState()

    private val _state = MutableStateFlow(initState())
    val state = _state
        .onStart { loadState() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000L),
            initState()
        )
    protected val currentState get() = state.value
    protected fun setState(block:  S.()-> S) = _state.update {
        val newValue = it.block()
        println(newValue)
        newValue
    }

    // 部分 ViewModel 功能不复杂，可以不实现Intent
    open fun sendIntent(intent: I) {}

    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect = _effect.receiveAsFlow()
    fun sendEffect(effect: E) = viewModelScope.launch { _effect.send(effect) }
}