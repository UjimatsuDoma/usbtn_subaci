package prac.tanken.shigure.ui.subaci.sources

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.sources.domain.SourcesUseCase
import prac.tanken.shigure.ui.subaci.sources.model.SourcesListItem
import prac.tanken.shigure.ui.subaci.sources.model.SourcesUiState
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val sourcesUseCase: SourcesUseCase,
    val myPlayer: MyPlayer,
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
                            Log.d(this@SourcesViewModel::class.simpleName, event.message)
                        }

                        is UseCaseEvent.Info -> {
                            Log.d(this@SourcesViewModel::class.simpleName, event.message)
                        }

                        UseCaseEvent.Loading -> {
                            uiState.value = SourcesUiState.Loading
                        }

                        is UseCaseEvent.Success<*> -> {
                            val newState = if (event.data is List<*>) {
                                val actualData = (event.data as Iterable<*>).filterIsInstance<SourcesListItem>()
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