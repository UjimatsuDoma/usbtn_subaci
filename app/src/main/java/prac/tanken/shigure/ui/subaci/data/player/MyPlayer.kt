package prac.tanken.shigure.ui.subaci.data.player

import android.content.res.AssetManager
import android.media.MediaPlayer
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import javax.inject.Inject

class MyPlayer @Inject constructor(
    val player: MediaPlayer,
    val am: AssetManager
) {
    fun stopIfPlaying() {
        if (player.isPlaying) {
            player.stop()
            player.reset()
        }
    }

    fun playByReference(vr: VoiceReference) {
        stopIfPlaying()
        val afd = am.openFd("subaciAudio/${vr.id}.mp3")
        player.apply {
            setDataSource(
                afd.fileDescriptor,
                afd.startOffset,
                afd.length
            )
            prepareAsync()
            setOnPreparedListener {
                start()
            }
            setOnCompletionListener {
                stop()
                reset()
            }
        }
    }
}