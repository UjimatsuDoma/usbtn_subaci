package prac.tanken.shigure.ui.subaci.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class LoadingViewModel() : ViewModel() {
    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    fun loading(
        dispatcher: CoroutineDispatcher = Dispatchers.Main,
        block: suspend CoroutineScope.() -> Unit
    ): Job =
        viewModelScope.launch(dispatcher) {
            _isLoading.value = true
            block()
            _isLoading.value = false
        }
}