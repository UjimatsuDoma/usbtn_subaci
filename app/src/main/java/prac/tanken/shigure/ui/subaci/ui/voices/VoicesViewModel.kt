package prac.tanken.shigure.ui.subaci.ui.voices

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.data.helper.DailyVoiceHelper.todayStr
import prac.tanken.shigure.ui.subaci.data.model.PlaylistEntity
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.data.model.voices.mutableVoiceGroups
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.repository.VoicesRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class VoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val voicesRepository: VoicesRepository,
    val playlistRepository: PlaylistRepository,
    val toastUtil: ToastUtil,
    val myPlayer: MyPlayer
) : LoadingViewModel() {
    // 每日随机语音
    private var _dailyVoiceUiState = mutableStateOf<DailyVoiceUiState>(DailyVoiceUiState.Loading)
    val dailyVoiceUiState get() = _dailyVoiceUiState

    // 当前选中的播放列表
    private var _selectedPlaylist = mutableStateOf<PlaylistEntity?>(null)
    private val selectedPlaylist get() = _selectedPlaylist

    // 当前选中的分组依据，以及分组的语音列表
    private var _voicesGrouped = mutableStateOf<VoicesGrouped?>(null)
    val voicesGrouped get() = _voicesGrouped

    init {
        viewModelScope.launch {
            launch { observeDailyVoice() }
            launch { observePlaylist() }
            launch { observeVoicesGroupedBy() }
        }
    }

    fun onButtonClicked(voiceReference: VoiceReference) =
        viewModelScope.launch(Dispatchers.Default) {
            myPlayer.playByReference(voiceReference)
        }

    private fun observeDailyVoice() = viewModelScope.launch(Dispatchers.IO) {
        voicesRepository.dailyVoiceEntityFlow
            .onEach { dailyVoice ->
                // check if daily voice exists AND is not expired
                val expired = dailyVoice?.addDate?.let { todayStr != it } == true
                if (dailyVoice == null || expired) {
                    viewModelScope.launch {
                        val voices = resRepository.loadVoices()
                        voicesRepository.updateDailyVoice(
                            voices[voices.indices.random()].id
                        )
                        toastUtil.toast("Rolled the dice...")
                    }
                }
            }
            .collect { dailyVoice ->
                val voices = resRepository.loadVoices()
                val dailyVoice = voices.filter { it.id == dailyVoice?.voiceId }
                val newState = if (dailyVoice.isNotEmpty()) {
                    DailyVoiceUiState.Loaded(dailyVoice[0])
                } else DailyVoiceUiState.Error
                _dailyVoiceUiState.value = newState
            }
    }

    private fun observePlaylist() = viewModelScope.launch(Dispatchers.IO) {
        playlistRepository.selectedPlaylist.collect {
            _selectedPlaylist.value = it
        }
    }

    private fun observeVoicesGroupedBy() = viewModelScope.launch {
        voicesRepository.voicesGroupedByFlow
            .collect { vgb ->
                if (vgb != null) {
                    loading {
                        _voicesGrouped.value = updateVoicesGroup(vgb)
                    }
                } else {
                    toastUtil.toast("Set to Kana.")
                    updateVoicesGroupedBy(VoicesGroupedBy.Kana)
                }
            }
    }

    private suspend fun updateVoicesGroup(voicesGroupedBy: VoicesGroupedBy): VoicesGrouped =
        withContext(Dispatchers.Default) {
            when (voicesGroupedBy) {
                VoicesGroupedBy.Category -> {
                    val categories = resRepository.loadCategories()
                    val voices = resRepository.loadVoices()
                    val voicesGrouped = mutableVoiceGroups().apply {
                        categories.forEach { category ->
                            val categoryVoices = category.idList.map { voiceRef ->
                                voices.filter { it.id == voiceRef.id }.toList()[0]
                            }.toList()
                            this.put(category.className, categoryVoices)
                        }
                    }
                    return@withContext VoicesGrouped.ByCategory(voicesGrouped)
                }

                VoicesGroupedBy.Kana -> {
                    val voices = resRepository.loadVoices()
                    val voicesGrouped = voices.groupBy { it.a }.mapKeys { entry ->
                        when (entry.key) {
                            "A" -> "あ行"
                            "KA" -> "か行"
                            "SA" -> "さ行"
                            "TA" -> "た行"
                            "NA" -> "な行"
                            "HA" -> "は行"
                            "MA" -> "ま行"
                            "YA" -> "や行"
                            "RA" -> "ら行"
                            "WA" -> "わ行"
                            else -> "その他"
                        }
                    }.toMap()
                    return@withContext VoicesGrouped.ByKana(voicesGrouped)
                }
            }
        }

    fun playDailyVoice() {
        if (dailyVoiceUiState.value is DailyVoiceUiState.Loaded) {
            val dailyVoice = (dailyVoiceUiState.value as DailyVoiceUiState.Loaded).voice
            myPlayer.playByReference(VoiceReference(dailyVoice.id))
            toastUtil.toast("Voice today: ${dailyVoice.label}")
        }
    }

    fun addToPlaylist(voiceReference: VoiceReference): Boolean =
        selectedPlaylist.value?.let { playlist ->
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

    fun updateVoicesGroupedBy(newValue: VoicesGroupedBy) = viewModelScope.launch {
        voicesRepository.updateVoicesGroupedBy(newValue)
    }
}