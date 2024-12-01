package prac.tanken.shigure.ui.subaci

import android.media.MediaPlayer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.all_voices.AllVoicesScreen
import prac.tanken.shigure.ui.subaci.category.CategoryVoicesScreen
import prac.tanken.shigure.ui.subaci.database.PlaylistDatabaseHelper
import prac.tanken.shigure.ui.subaci.model.Category
import prac.tanken.shigure.ui.subaci.model.Playlist
import prac.tanken.shigure.ui.subaci.model.Voice
import prac.tanken.shigure.ui.subaci.model.VoiceReference
import prac.tanken.shigure.ui.subaci.playlist.PlaylistScreen
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.util.parseJsonText
import prac.tanken.shigure.ui.subaci.util.readIStoText

class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private val dbHelper = PlaylistDatabaseHelper(this, "playlist.db", 1)

    private fun play(
        voiceId: String,
        volume: Float
    ) {
        mediaPlayer.apply {
            reset()
            assets.openFd("subaciAudio/${voiceId}.mp3").use {
                setDataSource(it.fileDescriptor, it.startOffset, it.length)
            }
            prepareAsync()
            setVolume(volume, volume)
            setOnPreparedListener {
                start()
            }
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
        val playlistDb = dbHelper.writableDatabase

        setContent {
            ShigureUiButtonAppComposeImplementationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var voices by remember { mutableStateOf<Array<Voice>>(emptyArray()) }
                    var categories by remember { mutableStateOf<Array<Category>>(emptyArray()) }
                    var isLoading by remember { mutableStateOf(true) }
                    var playlist by remember { mutableStateOf<Array<Voice>>(emptyArray()) }
                    var playlists by remember { mutableStateOf<Array<Playlist>>(emptyArray()) }
                    var isPlaying by remember { mutableStateOf(false) }
                    var playingIndex by remember { mutableStateOf(0) }
                    var screen by remember { mutableStateOf("all") }

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
                            val cursor =
                                playlistDb.query("Playlists", null, null, null, null, null, null)
                            val playLists = mutableListOf<Playlist>()
                            cursor.use {
                                it.apply {
                                    if (moveToFirst()) {
                                        do {
                                            val name = getString(getColumnIndexOrThrow("name"))
                                            val voicesStr =
                                                getString(getColumnIndexOrThrow("voices_string"))
                                            val voicesParsed =
                                                parseJsonText<Array<String>>(voicesStr)
                                            val voices = voicesParsed.map { VoiceReference(it) }
                                                .toTypedArray()
                                            playLists.add(Playlist(name, voices))
                                        } while (moveToNext())
                                    }
                                }
                            }
                            isLoading = false
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            if (!isLoading) {
                                when (screen) {
                                    "all" -> {
                                        AllVoicesScreen(
                                            voices = voices,
                                            onButtonClicked = {
                                                play(it.id, it.volume.toFloat())
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    "cat" -> {
                                        CategoryVoicesScreen(
                                            categories = categories,
                                            voices = voices,
                                            modifier = Modifier.fillMaxSize(),
                                            onButtonClicked = {
                                                play(it.id, it.volume.toFloat())
                                            },
                                        )
                                    }

                                    "list" -> {
                                        PlaylistScreen(
                                            playlist = playlist,
                                            playingIndex = playingIndex,
                                            onPlay = {
                                                playlist(0)
                                            },
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    else -> {
                                        Text("UNDER DEVELOPMENT")
                                    }
                                }
                            } else {
                                CircularProgressIndicator()
                            }
                        }
                        Row(
                            modifier = Modifier
                                .height(50.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = { screen = "all" }) {
                                    Text(
                                        text = "全部",
                                        fontWeight = if (screen == "all") FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = { screen = "cat" }) {
                                    Text(
                                        text = "分类",
                                        fontWeight = if (screen == "cat") FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                TextButton(onClick = { screen = "list" }) {
                                    Text(
                                        text = "列表",
                                        fontWeight = if (screen == "list") FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
