package prac.tanken.shigure.ui.subaci.ui.all_voices

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class AllVoicesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val myPlayer: MyPlayer
) : LoadingViewModel() {
    private var _voices = mutableStateListOf<Voice>()
    val voices get() = _voices

    init {
        loading(Dispatchers.IO) {
            _voices.addAll(resRepository.loadVoices())
        }
    }

    fun onButtonClicked(voiceReference: VoiceReference) =
        viewModelScope.launch(Dispatchers.Default) {
            myPlayer.playByReference(voiceReference)
        }
}