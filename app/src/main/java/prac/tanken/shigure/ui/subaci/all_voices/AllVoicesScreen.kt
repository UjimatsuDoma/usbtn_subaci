package prac.tanken.shigure.ui.subaci.all_voices

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import prac.tanken.shigure.ui.subaci.components.VoicesList
import prac.tanken.shigure.ui.subaci.model.Voice

@Composable
fun AllVoicesScreen(
    voices: Array<Voice>,
    modifier: Modifier = Modifier
) {
    VoicesList(
        voices = voices,
        modifier = modifier.fillMaxSize()
    )
}