package prac.tanken.shigure.ui.subaci.fontman.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.fontman.domain.FontCheckingState
import prac.tanken.shigure.ui.subaci.fontman.domain.FontCheckingUseCase
import prac.tanken.shigure.ui.subaci.fontman.domain.FontDecompressState
import prac.tanken.shigure.ui.subaci.fontman.domain.FontDecompressUseCase
import prac.tanken.shigure.ui.subaci.fontman.domain.FontInstantiateState
import prac.tanken.shigure.ui.subaci.fontman.domain.FontInstantiateUseCase

@HiltViewModel
class FontManagementViewModel @Inject constructor(
    private val checkingUseCase: FontCheckingUseCase,
    private val decompressUseCase: FontDecompressUseCase,
    private val instantiateUseCase: FontInstantiateUseCase,
) : ViewModel() {
    // 字体检查(c)、解压缩(d)和静态化(i)三个界面的状态
    private val _cState = MutableStateFlow<FontCheckingState>(FontCheckingState.Checking)
    val cState = _cState.asStateFlow()
    private val _dState = MutableStateFlow<FontDecompressState>(FontDecompressState.Intro)
    val dState = _dState.asStateFlow()
    private val _iState = MutableStateFlow<FontInstantiateState>(FontInstantiateState.Intro)
    val iState = _iState.asStateFlow()

    init {
        check()
        viewModelScope.launch {
            dState.collect {
                if (it is FontDecompressState.Complete) {
                    check()
                }
            }
        }
        viewModelScope.launch {
            iState.collect {
                if (it is FontInstantiateState.Complete) {
                    _cState.update { FontCheckingState.Passed }
                }
            }
        }
    }

    private fun check() {
        _cState.update { FontCheckingState.Checking }
        val variableReady = checkingUseCase.checkVariable()
        val staticReady = checkingUseCase.checkStatic()
        if (staticReady) {
            if (variableReady) {
                _cState.update { FontCheckingState.Cleaning }
                viewModelScope.launch(Dispatchers.IO) {
                    checkingUseCase.clearVariable()
                    _cState.update { FontCheckingState.Passed }
                }
            }
        } else {
            if (variableReady)
                _cState.update { FontCheckingState.NeedInstantiate }
            else
                _cState.update { FontCheckingState.NeedDecompress }
        }
    }

    fun gotoDecompress() {
        _dState.update { FontDecompressState.Progress(0f) }
        viewModelScope.launch {
            decompressUseCase.decompress { progress ->
                _dState.update { FontDecompressState.Progress(progress) }
            }
            _dState.update { FontDecompressState.Complete }
        }
    }

    fun onDecompressComplete() = _dState.update { FontDecompressState.Intro }

    fun gotoInstantiate() {
        _iState.update { FontInstantiateState.Progress(0f) }
        viewModelScope.launch {
            instantiateUseCase.instantiate { progress ->
                _iState.update { FontInstantiateState.Progress(progress) }
            }
            _iState.update { FontInstantiateState.Complete }
        }
    }

    fun onInstantiateComplete() = _iState.update { FontInstantiateState.Intro }
}