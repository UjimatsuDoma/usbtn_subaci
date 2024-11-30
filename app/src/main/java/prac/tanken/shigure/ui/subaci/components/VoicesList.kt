package prac.tanken.shigure.ui.subaci.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.model.Voice

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoicesList(
    voices: Array<Voice>,
    onButtonClicked: (Voice) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!voices.isEmpty()) {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(items = voices, key = { voice -> voice.id }) { voice ->
                Button(onClick = { onButtonClicked(voice) }) {
                    Text(text = voice.label)
                }
            }
        }
    } else {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("无内容")
        }
    }
}