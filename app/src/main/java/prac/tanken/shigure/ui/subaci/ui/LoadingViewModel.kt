package prac.tanken.shigure.ui.subaci.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

open class LoadingViewModel() : ViewModel() {
    var _isLoading = MutableStateFlow(false)
        private set
    open val isLoading = _isLoading.asStateFlow()

    fun loading(block: suspend CoroutineScope.() -> Unit): Job =
        viewModelScope.launch {
            _isLoading.value = true
            block()
            _isLoading.value = false
        }
}