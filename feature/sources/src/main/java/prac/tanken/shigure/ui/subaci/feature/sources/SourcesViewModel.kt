package prac.tanken.shigure.ui.subaci.feature.sources

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.feature.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.model.voices.toReference
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.domain.usecase.voices.GetVoicesUseCase
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.feature.base.mvi.BaseViewModel
import prac.tanken.shigure.ui.subaci.feature.sources.domain.SourcesUseCase
import prac.tanken.shigure.ui.subaci.feature.sources.model.SourcesListItem
import prac.tanken.shigure.ui.subaci.feature.sources.model.SourcesUiState
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    val sourcesUseCase: SourcesUseCase,
    val getVoicesUseCase: GetVoicesUseCase,
    val myPlayer: MyPlayer,
) : BaseViewModel<SourcesContract.State, SourcesContract.Intent, SourcesContract.Effect>() {
    override fun initState(): SourcesContract.State = SourcesContract.State()

    override fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSources()
        }
    }

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

    private suspend fun fetchSources() =
        getVoicesUseCase(VoicesGroupedBy.Video)
            .map { it as VoicesGrouped.ByVideo }
            .catch { throwable ->
                setState {
                    copy(
                        sourcesUiState = SourcesContract.SourcesUiState.Error
                            .fromThrowable(throwable)
                    )
                }
            }
            .collect { voicesGrouped ->
                val withVoices = voicesGrouped.voiceGroups.filter {
                    it.value.isNotEmpty()
                }
                val withoutVoices = voicesGrouped.voiceGroups.filter {
                    it.value.isEmpty()
                }.map { it.key }.toList()
                setState {
                    copy(
                        sourcesUiState = SourcesContract.SourcesUiState.Loaded(
                            sourcesWithVoice = VoicesGrouped.ByVideo(withVoices),
                            sourcesWithoutVoice = withoutVoices,
                        )
                    )
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
                                val actualData =
                                    (event.data as Iterable<*>).filterIsInstance<SourcesListItem>()
                                SourcesUiState.Loaded(actualData)
                            } else throw IllegalArgumentException()
                            uiState.value = newState
                        }
                    }
                }
        }
    }

    fun playByReference(voice: Voice) = myPlayer.playByReference(voice.toReference())
}