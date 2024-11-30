package prac.tanken.shigure.ui.subaci

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.model.Category
import prac.tanken.shigure.ui.subaci.model.Voice
import prac.tanken.shigure.ui.subaci.all_voices.AllVoicesScreen
import prac.tanken.shigure.ui.subaci.playlist.PlaylistScreen
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.util.parseJsonText
import prac.tanken.shigure.ui.subaci.util.readIStoText

class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer

    private fun play(
        voiceId: String,
    ) {
        mediaPlayer.apply {
            assets.openFd("subaciAudio/${voiceId}.mp3").use {
                setDataSource(it.fileDescriptor, it.startOffset, it.length)
            }
            prepare()
            start()
            setOnCompletionListener {
                stop()
                reset()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        mediaPlayer = MediaPlayer()

        setContent {
            ShigureUiButtonAppComposeImplementationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var voices by remember { mutableStateOf<Array<Voice>>(emptyArray()) }
                    var categories by remember { mutableStateOf<Array<Category>>(emptyArray()) }
                    var isLoading by remember { mutableStateOf(true) }
                    var playlist by remember { mutableStateOf<Array<Voice>>(emptyArray()) }
                    var isPlaying by remember { mutableStateOf(false) }
                    var playingIndex by remember { mutableStateOf(0) }

                    fun playlist(index: Int) {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.reset()
                        }
                        playingIndex = index
                        mediaPlayer.apply {
                            assets.openFd("subaciAudio/${playlist[playingIndex].id}.mp3")
                                .use {
                                    setDataSource(
                                        it.fileDescriptor,
                                        it.startOffset,
                                        it.length
                                    )
                                }
                            prepare()
                            start()
                            setOnCompletionListener {
                                stop()
                                reset()
                                if (playingIndex < playlist.lastIndex) {
                                    playlist(++playingIndex)
                                }
                            }
                        }
                    }

                    LaunchedEffect(true) {
                        launch(Dispatchers.IO) {
                            isLoading = true
                            val voicesParsed =
                                parseJsonText<Array<Voice>>(readIStoText(resources.openRawResource(R.raw.audio_list)))
                            voices = voicesParsed.sortedBy { it.k }.toTypedArray()
                            val categoriesParsed =
                                parseJsonText<Array<Category>>(
                                    readIStoText(
                                        resources.openRawResource(
                                            R.raw.class_list
                                        )
                                    )
                                )
                            categories = categoriesParsed.sortedBy { it.sectionId }
                                .toTypedArray()
                            playlist = voices.filter { voice ->
                                voice.id in categories[categories.indices.random()].idList.map { it.id }
                            }.toTypedArray()
                            isLoading = false
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!isLoading) {
//                            AllVoicesScreen(
//                                voices = voices,
//                                onButtonClicked = {
//                                    play(it.id)
//                                },
//                                modifier = Modifier.fillMaxSize()
//                            )
//                            CategoryVoicesScreen(
//                                categories = categories,
//                                voices = voices,
//                                modifier = Modifier.fillMaxSize(),
//                                onButtonClicked = {
//                                    play(it.id)
//                                },
//                            )
                            PlaylistScreen(
                                playlist = playlist,
                                playingIndex = playingIndex,
                                onPlay = {
                                    playlist(0)
                                },
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
