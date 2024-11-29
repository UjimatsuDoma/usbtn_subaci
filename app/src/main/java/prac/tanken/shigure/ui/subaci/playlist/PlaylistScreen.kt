package prac.tanken.shigure.ui.subaci.playlist

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
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
import prac.tanken.shigure.ui.subaci.model.Voice

@Composable
fun PlaylistScreen(
    playlist: Array<Voice>,
    playingIndex: Int?,
    onPlay: (Array<Voice>) -> Unit,
    modifier: Modifier = Modifier
) {
    playingIndex?.let {
        require(it in playlist.indices) {
            "Illegal playing index."
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(
                items = playlist,
                key = { index, voice -> index }
            ) { index, voice ->
                Text(
                    text = voice.label,
                    color = playingIndex?.let {
                        if (playingIndex == index) MaterialTheme.colorScheme.primary
                        else Color.White
                    } ?: Color.White
                )
            }
        }
        FloatingActionButton(
            onClick = {
                onPlay(playlist)
                println("clicked")
            },
            modifier = Modifier.padding(bottom = 16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PlayArrow,
                contentDescription = null
            )
        }
    }
}