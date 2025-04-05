package prac.tanken.shigure.ui.subaci.ui.voices

import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.domain.DailyVoiceUseCase
import prac.tanken.shigure.ui.subaci.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.domain.VoicesUseCase
import prac.tanken.shigure.ui.subaci.ui.voices.model.DailyVoiceUiState
import prac.tanken.shigure.ui.subaci.ui.voices.model.VoicesGroupedUiState
import prac.tanken.shigure.ui.subaci.ui.voices.model.VoicesUiState
import prac.tanken.shigure.ui.subaci.ui.voices.model.initialVoicesUiState
import javax.inject.Inject

@HiltViewModel
class VoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val dailyVoiceUseCase: DailyVoiceUseCase,
    val voicesUseCase: VoicesUseCase,
    val playlistUseCase: PlaylistUseCase,
    val toastUtil: ToastUtil,
    val myPlayer: MyPlayer
) : ViewModel() {
    // 新增：封装的UI状态
    var uiState = mutableStateOf<VoicesUiState>(initialVoicesUiState)
        private set

    // 新增：选中的播放列表的ID
    private var selectedPlaylistId = mutableLongStateOf(0L)

    init {
        observeDailyVoice()
        observePlaylist()
        observeVoicesGroupedBy()
    }

    // 该界面语音按钮的点击事件
    fun onButtonClicked(voiceReference: VoiceReference) =
        viewModelScope.launch(Dispatchers.Default) {
            myPlayer.playByReference(voiceReference)
        }

    // 启动数据流
    private fun observeDailyVoice() = viewModelScope.launch(Dispatchers.IO) {
        dailyVoiceUseCase.dailyVoiceEventFlow
            .collect { event ->
                when (event) {
                    is UseCaseEvent.Error -> {
                        val newState = DailyVoiceUiState.Error
                        uiState.value = uiState.value.copy(dailyVoiceUiState = newState)
                        launch(Dispatchers.Main) { toastUtil.toast(event.message) }
                    }

                    is UseCaseEvent.Info -> {
                        /**
                         * 思考：为什么不套launch会报“在没有Looper的线程调用toast”的错误？
                         * 答：线程名字叫DefaultDispatcher-worker
                         * 解决方案：Dispatchers.Main
                         */
                        launch(Dispatchers.Main) { toastUtil.toast(event.message) }
                    }

                    UseCaseEvent.Loading -> {
                        val newState = DailyVoiceUiState.StandBy
                        uiState.value = uiState.value.copy(dailyVoiceUiState = newState)
                    }

                    is UseCaseEvent.Success<*> -> {
                        val newState = if (event.data is Voice) {
                            DailyVoiceUiState.Loaded(event.data)
                        } else DailyVoiceUiState.Error
                        uiState.value = uiState.value.copy(dailyVoiceUiState = newState)
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
                        uiState.value = uiState.value.copy(voicesGroupedUiState = newState)
                        launch(Dispatchers.Main) { toastUtil.toast(event.message) }
                    }

                    is UseCaseEvent.Info -> {
                        launch(Dispatchers.Main) { toastUtil.toast(event.message) }
                    }

                    UseCaseEvent.Loading -> {
                        val newState = VoicesGroupedUiState.Loading
                        uiState.value = uiState.value.copy(voicesGroupedUiState = newState)
                    }

                    is UseCaseEvent.Success<*> -> {
                        val newState = if (event.data is VoicesGrouped) {
                            VoicesGroupedUiState.Success(event.data)
                        } else VoicesGroupedUiState.Error.fromThrowable(IllegalArgumentException())
                        uiState.value = uiState.value.copy(voicesGroupedUiState = newState)
                    }
                }
            }
    }

    fun playDailyVoice() {
        if (uiState.value.dailyVoiceUiState is DailyVoiceUiState.Loaded) {
            val actualState = uiState.value.dailyVoiceUiState as DailyVoiceUiState.Loaded
            val dailyVoice = actualState.voice
            myPlayer.playByReference(VoiceReference(dailyVoice.id))
            toastUtil.toast(buildString {
                append(resRepository.stringRes(R.string.daily_random_voice_play_prefix))
                append(dailyVoice.label)
            })
        }
    }

    fun addToPlaylist(voiceReference: VoiceReference) =
        viewModelScope.launch {
            if (selectedPlaylistId.longValue == 0L) {
                toastUtil.toast("No playlist selected.")
            } else {
                playlistUseCase.addToPlaylist(selectedPlaylistId.longValue, voiceReference.id)
            }
        }

    fun updateVoicesGroupedBy(newValue: VoicesGroupedBy) =
        viewModelScope.launch { voicesUseCase.updateVoicesGroupedBy(newValue) }
}