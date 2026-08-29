package prac.tanken.shigure.ui.subaci.feature.sources

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.model.voices.toReference
import prac.tanken.shigure.ui.subaci.core.domain.usecase.voices.GetVoicesUseCase
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.feature.base.mvi.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    val getVoicesUseCase: GetVoicesUseCase,
    val myPlayer: MyPlayer,
) : BaseViewModel<SourcesContract.State, SourcesContract.Intent, SourcesContract.Effect>() {
    override fun initState(): SourcesContract.State = SourcesContract.State()

    override fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            fetchSources()
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

    fun playByReference(voice: Voice) = myPlayer.playByReference(voice.toReference())
}