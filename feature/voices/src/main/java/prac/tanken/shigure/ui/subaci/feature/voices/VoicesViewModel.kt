package prac.tanken.shigure.ui.subaci.feature.voices

import androidx.compose.runtime.mutableLongStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.common.datetime.todayStr
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.model.voices.toReference
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.core.domain.usecase.voices.GetVoicesUseCase
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.feature.base.mvi.BaseViewModel
import prac.tanken.shigure.ui.subaci.feature.playlist.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.feature.voices.VoicesContract.Effect.DailyVoiceSnackbar
import prac.tanken.shigure.ui.subaci.feature.voices.VoicesContract.Effect.SettingsInitializationSnackbar
import prac.tanken.shigure.ui.subaci.feature.voices.VoicesContract.Effect.ShowSnackbar
import prac.tanken.shigure.ui.subaci.feature.voices.model.DailyVoiceUiState
import prac.tanken.shigure.ui.subaci.feature.voices.model.VoicesGroupedUiState.Error
import prac.tanken.shigure.ui.subaci.feature.voices.model.VoicesGroupedUiState.Loading
import prac.tanken.shigure.ui.subaci.feature.voices.model.VoicesGroupedUiState.Success
import prac.tanken.shigure.ui.subaci.feature.voices.model.VoicesSettingsState
import javax.inject.Inject

@HiltViewModel
class VoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val voicesRepository: VoicesRepository,
    val playlistUseCase: PlaylistUseCase,
    // for querying voices
    val getVoicesUseCase: GetVoicesUseCase,
    // for playing voices
    val myPlayer: MyPlayer
) : BaseViewModel<VoicesContract.State, VoicesContract.Intent, VoicesContract.Effect>() {

    override fun initState(): VoicesContract.State = VoicesContract.State()

    override fun loadState() {
        viewModelScope.launch(Dispatchers.IO) {
            observeVoicesSettings()
        }
        viewModelScope.launch(Dispatchers.IO) {
            observeVoicesGroupedBy()
        }
        observeDailyVoice()
        observePlaylist()
    }

    override fun sendIntent(intent: VoicesContract.Intent) {
        when (intent) {
            is VoicesContract.Intent.ChangeVoicesGroupedBy -> {
                updateVoicesGroupedBy(intent.newValue)
            }

            VoicesContract.Intent.PlayDailyVoice -> {
                if (state.value.dailyVoiceUiState is DailyVoiceUiState.Loaded) {
                    val actualState = state.value.dailyVoiceUiState as DailyVoiceUiState.Loaded
                    val dailyVoice = actualState.voice
                    myPlayer.playByReference(VoiceReference(dailyVoice.id))
                    sendEffect(DailyVoiceSnackbar(dailyVoice))
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

    private suspend fun observeVoicesSettings() =
        voicesRepository.voicesGroupedByFlow
            .collect { voicesGroupedBy ->
                if (voicesGroupedBy == null) {
                    updateVoicesGroupedBy(VoicesGroupedBy.Kana)
                    sendEffect(SettingsInitializationSnackbar)
                } else {
                    println("updating settings")
                    setState {
                        copy(
                            voicesSettingsState = VoicesSettingsState.Loaded(
                                voicesGroupedBy = voicesGroupedBy
                            )
                        )
                    }
                    println("updated settings")
                }
            }

    // TODO: business logic in viewmodel - is use case necessary since it's feature-specific?
    private fun observeDailyVoice() = viewModelScope.launch(Dispatchers.IO) {
        val voices = voicesRepository.voicesMetadata
            ?: error("Voices are not loaded yet.")

        val dailyVoiceEntityFlow = voicesRepository.dailyVoiceEntityFlow
        dailyVoiceEntityFlow
            .onEach { dailyVoiceEntity ->
                val expired = dailyVoiceEntity?.addDate?.let { todayStr != it } == true
                if (dailyVoiceEntity == null || expired) {
                    voicesRepository.updateDailyVoice(voices.random().id)
                    sendEffect(ShowSnackbar(resRepository.stringRes(R.string.daily_random_voice_refreshed)))
                }
            }
            .catch { throwable ->
                setState {
                    copy(
                        dailyVoiceUiState = DailyVoiceUiState.Error
                    )
                }
            }
            .collect { dailyVoiceEntity ->
                val voice = voices.firstOrNull { it.id == dailyVoiceEntity?.voiceId }
                if (voice == null) {
                    voicesRepository.updateDailyVoice(voices.random().id)
                    sendEffect(ShowSnackbar(resRepository.stringRes(R.string.daily_random_voice_refreshed)))
                } else {
                    setState {
                        copy(
                            dailyVoiceUiState = DailyVoiceUiState.Loaded(voice)
                        )
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

    private suspend fun observeVoicesGroupedBy(): Nothing =
        state.collect { state ->
            when (val state = state.voicesSettingsState) {
                is VoicesSettingsState.Loaded -> {
                    println("settings loaded")
                    val voicesGroupedBy = state.voicesGroupedBy
                    getVoicesUseCase(voicesGroupedBy)
                        .catch {
                            sendEffect(
                                ShowSnackbar(
                                    it.message ?: it.stackTraceToString()
                                )
                            )
                        }
                        .collect { voicesGrouped ->
                            when (voicesGrouped) {
                                is VoicesGrouped.ByCategory,
                                is VoicesGrouped.ByKana -> {
                                    setState {
                                        copy(
                                            voicesGroupedUiState = Success(
                                                voicesGrouped.voiceGroups
                                            )
                                        )
                                    }
                                }

                                is VoicesGrouped.ByNone -> {
                                    setState {
                                        copy(
                                            voicesGroupedUiState = Success(
                                                mapOf(
                                                    "" to voicesGrouped.voiceGroups[Unit].orEmpty()
                                                )
                                            )
                                        )
                                    }
                                }

                                is VoicesGrouped.ByVideo -> Unit
                            }
                        }
                }

                VoicesSettingsState.Loading -> {
                    setState {
                        copy(
                            voicesGroupedUiState = Loading
                        )
                    }
                }

                is VoicesSettingsState.Error -> {
                    setState {
                        copy(voicesGroupedUiState = Error(state.message))
                    }
                }
            }
        }

    fun addToPlaylist(voiceReference: VoiceReference) =
        viewModelScope.launch(Dispatchers.IO) {
            if (selectedPlaylistId.longValue == 0L) {
                sendEffect(ShowSnackbar(resRepository.stringRes(R.string.voices_info_no_playlist_selected)))
            } else {
                playlistUseCase.addToPlaylist(selectedPlaylistId.longValue, voiceReference.id)
            }
        }

    fun updateVoicesGroupedBy(newValue: VoicesGroupedBy) =
        viewModelScope.launch(Dispatchers.IO) { voicesRepository.updateVoicesGroupedBy(newValue) }
}