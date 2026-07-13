package prac.tanken.shigure.ui.subaci.feature.voices

import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.model.voices.toReference
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.feature.base.SnackbarMessage
import prac.tanken.shigure.ui.subaci.feature.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.feature.base.mvi.BaseViewModel
import prac.tanken.shigure.ui.subaci.feature.playlist.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.feature.voices.domain.DailyVoiceUseCase
import prac.tanken.shigure.ui.subaci.feature.voices.domain.VoicesUseCase
import prac.tanken.shigure.ui.subaci.feature.voices.model.VoicesGrouped
import javax.inject.Inject

@HiltViewModel
class VoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val dailyVoiceUseCase: DailyVoiceUseCase,
    val voicesUseCase: VoicesUseCase,
    val playlistUseCase: PlaylistUseCase,
    val myPlayer: MyPlayer
) : BaseViewModel<VoicesContract.State, VoicesContract.Intent, VoicesContract.Effect>() {

    override fun initState(): VoicesContract.State = VoicesContract.State()

    override fun sendIntent(intent: VoicesContract.Intent) {
        when(intent) {
            is VoicesContract.Intent.ChangeVoicesGroupedBy -> {
                updateVoicesGroupedBy(intent.newValue)
            }
            VoicesContract.Intent.PlayDailyVoice -> {
                if (state.value.dailyVoiceUiState is DailyVoiceUiState.Loaded) {
                    val actualState = state.value.dailyVoiceUiState as DailyVoiceUiState.Loaded
                    val dailyVoice = actualState.voice
                    myPlayer.playByReference(VoiceReference(dailyVoice.id))
                    sendEffect(VoicesContract.Effect.ShowDailyVoiceToast(dailyVoice))
                }
            }
            is VoicesContract.Intent.PlayVoice -> {
                viewModelScope.launch(Dispatchers.Default) {
                    myPlayer.playByReference(intent.voice.toReference())
                }
            }
        }
    }

    // 新增：选中的播放列表的ID
    private var selectedPlaylistId = mutableLongStateOf(0L)

    init {
        observeDailyVoice()
        observePlaylist()
        observeVoicesGroupedBy()
    }

    // 启动数据流
    private fun observeDailyVoice() = viewModelScope.launch(Dispatchers.IO) {
        dailyVoiceUseCase.dailyVoiceEventFlow
            .collect { event ->
                when (event) {
                    is UseCaseEvent.Error -> {
                        val newState = DailyVoiceUiState.Error
                        setState {
                            copy(dailyVoiceUiState = newState)
                        }
                        sendEffect(VoicesContract.Effect.ShowToast(event.message))
                    }

                    is UseCaseEvent.Info -> {
                        sendEffect(VoicesContract.Effect.ShowToast(event.message))
                    }

                    UseCaseEvent.Loading -> {
                        val newState = DailyVoiceUiState.StandBy
                        setState {
                            copy(dailyVoiceUiState = newState)
                        }
                    }

                    is UseCaseEvent.Success<*> -> {
                        val newState = if (event.data is Voice) {
                            DailyVoiceUiState.Loaded(event.data as Voice)
                        } else DailyVoiceUiState.Error
                        setState {
                            copy(dailyVoiceUiState = newState)
                        }
                    }
                }
            }
    }

    private fun observePlaylist() = viewModelScope.launch {
        playlistUseCase.playlistSelectedFlow
            .collect { plistSelected ->
                selectedPlaylistId.longValue = plistSelected.selectedId
            }
    }

    private fun observeVoicesGroupedBy() = viewModelScope.launch {
        voicesUseCase.voicesGroupedEventFlow
            .collect { event ->
                when (event) {
                    is UseCaseEvent.Error -> {
                        val newState = VoicesGroupedUiState.Error(event.message)
                        setState {
                            copy(voicesGroupedUiState = newState)
                        }
                        sendEffect(VoicesContract.Effect.ShowToast(event.message))
                    }

                    is UseCaseEvent.Info -> {
                        sendEffect(VoicesContract.Effect.ShowToast(event.message))
                    }

                    UseCaseEvent.Loading -> {
                        val newState = VoicesGroupedUiState.Loading
                        setState {
                            copy(voicesGroupedUiState = newState)
                        }
                    }

                    is UseCaseEvent.Success<*> -> {
                        val newState = if (event.data is VoicesGrouped) {
                            VoicesGroupedUiState.Success(event.data as VoicesGrouped)
                        } else VoicesGroupedUiState.Error.fromThrowable(IllegalArgumentException())
                        setState {
                            copy(voicesGroupedUiState = newState)
                        }
                    }
                }
            }
    }

    fun addToPlaylist(voiceReference: VoiceReference) =
        viewModelScope.launch {
            if (selectedPlaylistId.longValue == 0L) {
                sendEffect(VoicesContract.Effect.ShowToast(resRepository.stringRes(R.string.voices_info_no_playlist_selected)))
            } else {
                playlistUseCase.addToPlaylist(selectedPlaylistId.longValue, voiceReference.id)
            }
        }

    fun updateVoicesGroupedBy(newValue: VoicesGroupedBy) =
        viewModelScope.launch { voicesUseCase.updateVoicesGroupedBy(newValue) }
}