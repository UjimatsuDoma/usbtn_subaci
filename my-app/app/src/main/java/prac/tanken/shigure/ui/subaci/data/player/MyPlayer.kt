package prac.tanken.shigure.ui.subaci.data.player

import android.media.MediaPlayer
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import javax.inject.Inject

class MyPlayer @Inject constructor(
    val player: MediaPlayer,
    val resRepository: ResRepository,
) {
    var isLooping = false
        private set

    fun toggleLooping(newValue: Boolean) {
        isLooping = newValue
    }

    fun stopIfPlaying() {
        if (player.isPlaying) {
            player.stop()
            player.reset()
        }
    }

    fun playByReference(
        vr: VoiceReference,
        onStart: () -> Unit = {},
        onComplete: () -> Unit = {},
    ) {
        stopIfPlaying()
        val afd = resRepository.getVoiceAFD(vr)
        with(player) {
            setDataSource(
                afd.fileDescriptor,
                afd.startOffset,
                afd.length
            )
            prepareAsync()
            setOnPreparedListener {
                onStart()
                start()
            }
            setOnCompletionListener {
                stop()
                reset()
                onComplete()
            }
        }
    }

    fun playByList(
        list: List<VoiceReference>,
        onStart: (Int) -> Unit = {},
        onComplete: () -> Unit = {},
    ) {
        fun play(index: Int) {
            playByReference(
                vr = list[index],
                onStart = { onStart(index) },
                onComplete = {
                    if (index < list.lastIndex)
                        play(index + 1)
                    else if (isLooping)
                        play(0)
                    else onComplete()
                }
            )
        }
        play(0)
    }
}