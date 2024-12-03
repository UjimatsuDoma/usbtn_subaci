package prac.tanken.shigure.ui.subaci.ui.playlist

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.data.model.Playlist
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaylistScreen(
    playlist: Playlist,
    playingIndex: Int?,
    voices: Array<Voice>,
    onPlay: (Playlist) -> Unit,
    onStop: () -> Unit,
    // true = move up
    onMove: (Int, Boolean) -> Unit,
    onDelete: (VoiceReference) -> Unit,
    modifier: Modifier = Modifier
) {
    playingIndex?.let {
        require(it in (-1..playlist.voices.lastIndex)) {
            "Illegal playing index."
        }
    }
    val playing = playingIndex != -1

    LaunchedEffect(playlist) {
        println("playlist changed")
        println(playlist.toString())
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (playlist.voices.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(
                    items = playlist.voices,
                    key = { index, voice -> voice.id }
                ) { index, voice ->
                    val defaultTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black
                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        modifier = Modifier
                            .combinedClickable(
                                onClick = {},
                                onLongClick = {
                                    if (!playing) expanded = true
                                }
                            )
                    ) {
                        Text(
                            text = voices.filter { it.id == voice.id }[0].label,
                            color = playingIndex?.let {
                                if (playingIndex == index) MaterialTheme.colorScheme.primary
                                else defaultTextColor
                            } ?: defaultTextColor,
                            modifier = Modifier.padding(8.dp)
                        )

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = {
                                expanded = false
                            }
                        ) {
                            DropdownMenuItem(
                                text = { Text("上移") },
                                enabled = index != 0,
                                onClick = {
                                    onMove(index, true)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("上移") },
                                enabled = index != playlist.voices.lastIndex,
                                onClick = {
                                    onMove(index, false)
                                    expanded = false
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("删除") },
                                enabled = index != playlist.voices.lastIndex,
                                onClick = {
                                    onDelete(voice)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            FloatingActionButton(
                onClick = {
                    if (!playing) onPlay(playlist)
                    else onStop()
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .align(Alignment.BottomCenter)
            ) {
                Icon(
                    imageVector = if (!playing) Icons.Default.PlayArrow else Icons.Default.Close,
                    contentDescription = null
                )
            }
        } else {
            Text("播放列表无内容")
        }
    }

}