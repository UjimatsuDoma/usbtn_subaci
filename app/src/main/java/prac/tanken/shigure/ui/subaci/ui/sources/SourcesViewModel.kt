package prac.tanken.shigure.ui.subaci.ui.sources

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.domain.SourcesUseCase
import prac.tanken.shigure.ui.subaci.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.ui.sources.model.SourcesListItem
import prac.tanken.shigure.ui.subaci.ui.sources.model.SourcesUiState
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val sourcesUseCase: SourcesUseCase,
    val myPlayer: MyPlayer,
    val toastUtil: ToastUtil,
) : ViewModel() {
    var uiState = mutableStateOf<SourcesUiState>(SourcesUiState.StandBy)
        private set

    // 协程相关
    private fun sourcesCoroutine(
        dispatcher: CoroutineDispatcher = Dispatchers.Default,
        block: suspend CoroutineScope.() -> Unit
    ) {
        val sourcesViewModelScopeExceptionHandler = CoroutineExceptionHandler { _, throwable ->
            throwable.printStackTrace()
            uiState.value = SourcesUiState.Error(throwable.message ?: "")
        }
        val context =
            viewModelScope.coroutineContext + dispatcher + sourcesViewModelScopeExceptionHandler

        viewModelScope.launch(context) {
            block()
        }
    }

    init {
        sourcesCoroutine {
            sourcesUseCase.sourcesEventFlow
                .collect { event ->
                    when (event) {
                        is UseCaseEvent.Error -> {
                            uiState.value = SourcesUiState.Error(event.message)
                            toastUtil.toast(event.message)
                        }

                        is UseCaseEvent.Info -> {
                            toastUtil.toast(event.message)
                        }

                        UseCaseEvent.Loading -> {
                            uiState.value = SourcesUiState.Loading
                        }

                        is UseCaseEvent.Success<*> -> {
                            val newState = if (event.data is List<*>) {
                                val actualData = event.data.filterIsInstance<SourcesListItem>()
                                SourcesUiState.Loaded(actualData)
                            } else throw IllegalArgumentException()
                            uiState.value = newState
                        }
                    }
                }
        }
    }

    fun playByReference(voiceReference: VoiceReference) = myPlayer.playByReference(voiceReference)
}