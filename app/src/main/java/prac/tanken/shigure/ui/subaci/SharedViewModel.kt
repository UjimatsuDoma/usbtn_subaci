package prac.tanken.shigure.ui.subaci

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.repository.ResRepository
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    resRepository: ResRepository
) : ViewModel() {
    private var _voices = MutableStateFlow<List<Voice>>(emptyList())
    val voices = _voices.asStateFlow()
    private var _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            _isLoading.value = true
            resRepository.loadVoices().collect { list ->
                _voices.value = list
            }
            _isLoading.value = false
        }
    }
}