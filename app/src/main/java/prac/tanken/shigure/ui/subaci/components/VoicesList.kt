package prac.tanken.shigure.ui.subaci.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.model.Voice
import prac.tanken.shigure.ui.subaci.model.VoiceReference

@OptIn(ExperimentalLayoutApi::class, ExperimentalFoundationApi::class)
@Composable
fun VoicesList(
    voices: Array<Voice>,
    onButtonClicked: (Voice) -> Unit,
    onAddList: (VoiceReference) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!voices.isEmpty()) {
//        LazyColumn(
//            modifier = modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(items = voices, key = { voice -> voice.id }) { voice ->
//                Button(onClick = { onButtonClicked(voice) }) {
//                    Text(text = voice.label)
//                }
//            }
//        }
        FlowRow(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            voices.forEach { voice ->
                Column {
                    var expanded by remember { mutableStateOf(false) }

                    LongPressButton(
                        onClick = { onButtonClicked(voice) },
                        onLongClick = { expanded = true },
                        modifier = Modifier.wrapContentSize()
                    ) {
                        Text(
                            text = voice.label,
                            style = MaterialTheme.typography.labelLarge,
                        )
                        voice.new?.let {
                            if (it) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Badge {
                                    Text("NEW")
                                }
                            }
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("加入播放列表") },
                            onClick = {
                                println("add playlist clicked")
                                onAddList(VoiceReference(voice.id))
                                expanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("查看语音来源") },
                            enabled = voice.videoId != null,
                            onClick = {
                                expanded = false
                            }
                        )
                    }
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