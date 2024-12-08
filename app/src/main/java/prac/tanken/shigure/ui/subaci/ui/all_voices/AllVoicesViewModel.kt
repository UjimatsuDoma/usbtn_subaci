package prac.tanken.shigure.ui.subaci.ui.all_voices

import dagger.hilt.android.lifecycle.HiltViewModel
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
    lateinit var voices: List<Voice>

    init {
        loading {
            voices = resRepository.loadVoices()
        }
    }

    fun onButtonClicked(voiceReference: VoiceReference) =
        myPlayer.playByReference(voiceReference)
}