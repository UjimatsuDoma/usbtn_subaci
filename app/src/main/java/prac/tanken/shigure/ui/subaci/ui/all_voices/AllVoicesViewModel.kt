package prac.tanken.shigure.ui.subaci.ui.all_voices

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.data.model.DailyVoice
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.DailyVoiceRepository
import prac.tanken.shigure.ui.subaci.data.repository.DailyVoiceRepository.Companion.todayStr
import prac.tanken.shigure.ui.subaci.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class AllVoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val dailyVoiceRepository: DailyVoiceRepository,
    val playlistRepository: PlaylistRepository,
    val toastUtil: ToastUtil,
    val myPlayer: MyPlayer
) : LoadingViewModel() {
    private var _voices = mutableStateListOf<Voice>()
    val voices get() = _voices
    private var _dailyVoice = mutableStateOf<DailyVoice?>(null)
    val dailyVoice get() = _dailyVoice
    private var _selectedPlaylist = mutableStateOf<PlaylistEntity?>(null)
    private val selectedPlaylist get() = _selectedPlaylist.value

    init {
        loading(Dispatchers.IO) {
            _voices.addAll(resRepository.loadVoices())
            viewModelScope.launch {
                launch { observeDailyVoice() }
                launch { observePlaylist() }
            }
        }
    }

    fun onButtonClicked(voiceReference: VoiceReference) =
        viewModelScope.launch(Dispatchers.Default) {
            myPlayer.playByReference(voiceReference)
        }

    private suspend fun observeDailyVoice() = withContext(Dispatchers.IO) {
        dailyVoiceRepository.dailyVoiceFlow
            .onEach { dailyVoice ->
                // check if daily voice exists AND is not expired
                val expired = dailyVoice?.addDate?.let { todayStr != it } == true
                if (dailyVoice == null || expired) {
                    viewModelScope.launch {
                        dailyVoiceRepository.updateDailyVoice(
                            voices[voices.indices.random()].id
                        )
                        toastUtil.toast("Rolled the dice...")
                    }
                }
            }
            .collect { _dailyVoice.value = it }
    }

    private suspend fun observePlaylist() = withContext(Dispatchers.IO) {
        playlistRepository.selectedPlaylist.collect {
            _selectedPlaylist.value = it
        }
    }

    fun playDailyVoice() {
        dailyVoice.value?.let { dailyVoice ->
            myPlayer.playByReference(VoiceReference(dailyVoice.voiceId))
            val label = voices.filter { it.id == dailyVoice.voiceId }.toList()[0].label
            toastUtil.toast("Voice today: $label")
        }
    }

    fun addToPlaylist(voiceReference: VoiceReference): Boolean =
        selectedPlaylist?.let { playlist ->
            val arr = parseJsonString<List<String>>(playlist.playlistItems)
            if (voiceReference.id in arr) {
                toastUtil.toast("Already exists.")
                return@let false
            } else {
                val newArr = arr.toMutableList().apply { add(voiceReference.id) }.toList()
                val newArrStr = Json.encodeToString(newArr)
                val newPlaylist = playlist.copy(playlistItems = newArrStr)
                viewModelScope.launch(Dispatchers.IO) {
                    playlistRepository.updatePlaylist(newPlaylist)
                }
                return@let true
            }
        } ?: {
            toastUtil.toast("No playlist selected.")
            false
        }.invoke()
}