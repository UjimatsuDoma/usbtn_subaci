package prac.tanken.shigure.ui.subaci.ui.all_voices

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import prac.tanken.shigure.ui.subaci.data.model.DailyVoice
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.DailyVoiceRepository
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.util.ToastUtil
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class AllVoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val dailyVoiceRepository: DailyVoiceRepository,
    val toastUtil: ToastUtil,
    val myPlayer: MyPlayer
) : LoadingViewModel() {
    private var _voices = mutableStateListOf<Voice>()
    val voices get() = _voices
    private var _dailyVoice = mutableStateOf<DailyVoice?>(null)
    val dailyVoice get() = _dailyVoice

    init {
        loading(Dispatchers.IO) {
            _voices.addAll(resRepository.loadVoices())
        }
        viewModelScope.launch {
            dailyVoiceRepository.dailyVoiceFlow
                .collect { _dailyVoice.value = it }
        }
    }

    fun onButtonClicked(voiceReference: VoiceReference) =
        viewModelScope.launch(Dispatchers.Default) {
            myPlayer.playByReference(voiceReference)
        }

    fun playDailyVoice() {
        // check if daily voice exists AND is not expired
        val expired = {
            fun encode(date: LocalDate) = LocalDate.Formats.ISO_BASIC.format(date)
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            dailyVoice.value?.addDate?.let { encode(today) != encode(it) } == true
        }.invoke()
        println(dailyVoice.value == null)
        if (dailyVoice.value == null || expired) {
            viewModelScope.launch {
                dailyVoiceRepository.updateDailyVoice(
                    voices[voices.indices.random()].id
                )
            }
            toastUtil.toast("Rolled the dice...")
        }
        dailyVoice.value?.let { dailyVoice ->
            myPlayer.playByReference(VoiceReference(dailyVoice.voiceId))
            val label = voices.filter { it.id == dailyVoice.voiceId }.toList()[0].label
            toastUtil.toast("Voice today: $label")
        }
    }
}