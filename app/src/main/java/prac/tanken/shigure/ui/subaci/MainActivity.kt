package prac.tanken.shigure.ui.subaci

import android.content.ContentValues
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
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

@OptIn(ExperimentalMaterial3Api::class)
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

    private fun stop() {
        mediaPlayer.apply {
            stop()
            reset()
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
                    var playlist by remember {
                        mutableStateOf<Playlist>(
                            Playlist(
                                "No playlists",
                                emptyArray()
                            )
                        )
                    }
                    var playlists by remember { mutableStateOf<Array<Playlist>>(emptyArray()) }
                    var playingIndex by remember { mutableIntStateOf(-1) }
                    var screen by remember { mutableStateOf("all") }

                    fun playlist(index: Int) {
                        if (mediaPlayer.isPlaying) {
                            mediaPlayer.reset()
                        }
                        playingIndex = index
                        mediaPlayer.apply {
                            assets.openFd("subaciAudio/${playlist.voices[playingIndex].id}.mp3")
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
                                if (playingIndex < playlist.voices.lastIndex) {
                                    playlist(++playingIndex)
                                }
                            }
                        }
                    }

                    fun stopList() {
                        stop()
                        playingIndex = -1
                    }

                    fun getPlaylists() {
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
                        playlists = playLists.toTypedArray()
                    }

                    fun newPlaylist(name: String) {
                        if (playlists.filter { it.name == name }.toTypedArray().isNotEmpty()) {
                            return
                        }
                        playlists = playlists.toMutableList().apply {
                            add(Playlist(name, emptyArray()))
                        }.toTypedArray()
                        val voicesStr =
                            Json.encodeToString<Array<String>>(emptyArray())
                        val cv = ContentValues().apply {
                            put("name", name)
                            put("voices_string", voicesStr)
                        }
                        playlistDb.insert("Playlists", null, cv)
                        playlist = playlists.filter { it.name == name }.toTypedArray()[0]
                    }

                    fun deletePlaylist(name: String) {
                        println(name)
                        playlists = playlists.filter { it.name != name }.toTypedArray()
                        playlist = playlists[0]
                        playlistDb.delete("Playlists", "name = ?", arrayOf(name))
                    }

                    fun updatePlaylist(pl: Playlist) {
                        playlist = pl
                        if (playlists.filter { it.name == pl.name }.toTypedArray().isEmpty())
                            return
                        playlists = playlists.map {
                            if (it.name != pl.name) it
                            else it.copy(voices = pl.voices)
                        }.toTypedArray()
                        val voicesStr =
                            Json.encodeToString<Array<String>>(
                                pl.voices.map { it.id }.toTypedArray()
                            )
                        val cv = ContentValues().apply {
                            put("voices_string", voicesStr)
                        }
                        playlistDb.update("Playlists", cv, "name = ?", arrayOf(pl.name))
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
                            getPlaylists()
                            if (playlists.isEmpty()) {
                                newPlaylist("Playlist 1")
                            }
                            playlist = playlists[0]
                            isLoading = false
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (screen) {
                            "all" -> {}
                            "cat" -> {}
                            "list" -> {
                                TopAppBar(
                                    title = {
                                        Column {
                                            var expanded by remember {
                                                mutableStateOf(
                                                    false
                                                )
                                            }

                                            Row(
                                                modifier = Modifier
                                                    .clickable {
                                                        expanded = !expanded
                                                    }
                                            ) {
                                                Text(
                                                    text = playlist.name
                                                )
                                                Icon(
                                                    imageVector = if (expanded) {
                                                        Icons.Default.KeyboardArrowUp
                                                    } else {
                                                        Icons.Default.KeyboardArrowDown
                                                    },
                                                    contentDescription = null
                                                )
                                            }

                                            DropdownMenu(
                                                expanded = expanded,
                                                onDismissRequest = {
                                                    expanded = false
                                                }
                                            ) {
                                                playlists.forEach { pl ->
                                                    DropdownMenuItem(
                                                        text = { Text(pl.name) },
                                                        onClick = {
                                                            playlist =
                                                                playlists.filter { it.name == pl.name }
                                                                    .toTypedArray()[0]
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    },
                                    actions = {
                                        IconButton(onClick = {
                                            val rand = buildString {
                                                repeat(8) {
                                                    append(
                                                        listOf(('0'..'9'), ('a'..'z'), ('A'..'Z'))
                                                            .flatten().random()
                                                    )
                                                }
                                            }
                                            newPlaylist(rand)
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Add,
                                                contentDescription = null
                                            )
                                        }
                                        IconButton(onClick = {
                                            if (playlists.size > 1) {
                                                deletePlaylist(playlist.name)
                                            } else {
                                                Toast.makeText(
                                                    this@MainActivity,
                                                    "cannot delete this only playlist",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                        }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = null
                                            )
                                        }
                                    }
                                )
                            }

                            else -> {}
                        }
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
                                            onAddList = {
                                                updatePlaylist(
                                                    playlist.copy(
                                                        voices = playlist.voices.toMutableSet()
                                                            .apply {
                                                                add(it)
                                                            }.toTypedArray()
                                                    )
                                                )
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
                                            onAddList = {
                                                updatePlaylist(
                                                    playlist.copy(
                                                        voices = playlist.voices.toMutableSet()
                                                            .apply {
                                                                add(it)
                                                            }.toTypedArray()
                                                    )
                                                )
                                            },
                                        )
                                    }

                                    "list" -> {
                                        Column {
                                            PlaylistScreen(
                                                playlist = playlist,
                                                playingIndex = playingIndex,
                                                onPlay = { playlist(0) },
                                                onStop = { stopList() },
                                                voices = voices,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }
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
