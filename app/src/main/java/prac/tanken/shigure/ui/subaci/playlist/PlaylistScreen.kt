package prac.tanken.shigure.ui.subaci.playlist

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.model.Playlist
import prac.tanken.shigure.ui.subaci.model.Voice

@Composable
fun PlaylistScreen(
    playlist: Playlist,
    playingIndex: Int?,
    voices: Array<Voice>,
    onPlay: (Playlist) -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    playingIndex?.let {
        require(it in (-1..playlist.voices.lastIndex)) {
            "Illegal playing index."
        }
    }
    val playing = playingIndex != -1

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
                    key = { index, voice -> index }
                ) { index, voice ->
                    val defaultTextColor = if (isSystemInDarkTheme()) Color.White else Color.Black

                    Text(
                        text = voices.filter { it.id == voice.id }[0].label,
                        color = playingIndex?.let {
                            if (playingIndex == index) MaterialTheme.colorScheme.primary
                            else defaultTextColor
                        } ?: defaultTextColor
                    )
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