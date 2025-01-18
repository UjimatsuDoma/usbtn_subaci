package prac.tanken.shigure.ui.subaci.ui.sources

import androidx.compose.runtime.mutableStateOf
import dagger.hilt.android.lifecycle.HiltViewModel
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import prac.tanken.shigure.ui.subaci.ui.sources.model.SourcesListItem
import javax.inject.Inject

@HiltViewModel
class SourcesViewModel @Inject constructor(
    val resRepository: ResRepository,
    val myPlayer: MyPlayer,
) : LoadingViewModel() {
    private var _sources = mutableStateOf<List<SourcesListItem>>(emptyList())
    val sources get() = _sources

    init {
        loading {
            val allVoices = resRepository.loadVoices()
            val newList = resRepository.loadSources().map { sourceEntity ->
                val voices = allVoices.filter { it.videoId == sourceEntity.videoId }.toList()
                SourcesListItem(sourceEntity.videoId, sourceEntity.title, voices)
            }.toList()
            _sources.value = newList
        }
    }

    fun playByReference(voiceReference: VoiceReference) = myPlayer.playByReference(voiceReference)
}