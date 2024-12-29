package prac.tanken.shigure.ui.subaci.data.player

import android.content.res.AssetManager
import android.media.MediaPlayer
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.data.util.CallbackInvokedAsIs
import javax.inject.Inject

class MyPlayer @Inject constructor(
    val player: MediaPlayer,
    val am: AssetManager
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
        onStart: CallbackInvokedAsIs = {},
        onComplete: CallbackInvokedAsIs = {},
    ) {
        stopIfPlaying()
        val afd = am.openFd("subaciAudio/${vr.id}.mp3")
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
//                    onComplete()
                    if (index < list.lastIndex)
                        play(index + 1)
                    else if(isLooping)
                        play(0)
                    else onComplete()
                }
            )
        }
        play(0)
    }
}