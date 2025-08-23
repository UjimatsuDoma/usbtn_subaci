package prac.tanken.shigure.ui.subaci.voices

import android.util.Log
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.base.model.voice.VoicesGrouped
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.player.MyPlayer
import prac.tanken.shigure.ui.subaci.playlist.domain.PlaylistUseCase
import prac.tanken.shigure.ui.subaci.voices.domain.DailyVoiceUseCase
import prac.tanken.shigure.ui.subaci.voices.domain.VoicesUseCase
import prac.tanken.shigure.ui.subaci.voices.model.VoicesGroupedUiState
import prac.tanken.shigure.ui.subaci.voices.model.daily.DailyVoiceUiState
import prac.tanken.shigure.ui.subaci.voices.model.initialVoicesUiState
import javax.inject.Inject

@HiltViewModel
class VoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val dailyVoiceUseCase: DailyVoiceUseCase,
    val voicesUseCase: VoicesUseCase,
    val playlistUseCase: PlaylistUseCase,
    val myPlayer: MyPlayer
) : ViewModel() {
    // 新增：封装的UI状态
    var uiState = mutableStateOf(initialVoicesUiState)
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
                        launch(Dispatchers.Main) { Log.d(this@VoicesViewModel::class.simpleName, event.message) }
                    }

                    is UseCaseEvent.Info -> {
                        /**
                         * 思考：为什么不套launch会报“在没有Looper的线程调用toast”的错误？
                         * 答：线程名字叫DefaultDispatcher-worker
                         * 解决方案：Dispatchers.Main
                         */
                        launch(Dispatchers.Main) { Log.d(this@VoicesViewModel::class.simpleName, event.message) }
                    }

                    UseCaseEvent.Loading -> {
                        val newState = DailyVoiceUiState.StandBy
                        uiState.value = uiState.value.copy(dailyVoiceUiState = newState)
                    }

                    is UseCaseEvent.Success<*> -> {
                        val newState = if (event.data is Voice) {
                            DailyVoiceUiState.Loaded(event.data as Voice)
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
                        launch(Dispatchers.Main) { Log.d(this@VoicesViewModel::class.simpleName, event.message) }
                    }

                    is UseCaseEvent.Info -> {
                        launch(Dispatchers.Main) { Log.d(this@VoicesViewModel::class.simpleName, event.message) }
                    }

                    UseCaseEvent.Loading -> {
                        val newState = VoicesGroupedUiState.Loading
                        uiState.value = uiState.value.copy(voicesGroupedUiState = newState)
                    }

                    is UseCaseEvent.Success<*> -> {
                        val newState = if (event.data is VoicesGrouped) {
                            VoicesGroupedUiState.Success(event.data as VoicesGrouped)
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
            val message = buildString {
                append(resRepository.stringRes(R.string.daily_random_voice_play_prefix))
                append(dailyVoice.label)
            }
            Log.d(this@VoicesViewModel::class.simpleName, message)
        }
    }

    fun addToPlaylist(voiceReference: VoiceReference) =
        viewModelScope.launch {
            if (selectedPlaylistId.longValue == 0L) {
                Log.d(this@VoicesViewModel::class.simpleName, "No playlist selected.")
            } else {
                playlistUseCase.addToPlaylist(selectedPlaylistId.longValue, voiceReference.id)
            }
        }

    fun updateVoicesGroupedBy(newValue: VoicesGroupedBy) =
        viewModelScope.launch { voicesUseCase.updateVoicesGroupedBy(newValue) }
}