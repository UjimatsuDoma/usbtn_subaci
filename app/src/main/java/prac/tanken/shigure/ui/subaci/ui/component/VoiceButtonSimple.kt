package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.ui.theme.NotoSansJP

@Composable
fun VoiceButtonSimple(
    voice: Voice,
    modifier: Modifier = Modifier,
    onPlay: (VoiceReference) -> Unit = {}
) {
    AdvancedButton(
        onClick = { onPlay(voice.toReference()) },
        contentFontFamily = NotoSansJP,
        modifier = modifier
    ) {
        Text(text = voice.label)
        if (voice.new == true) {
            Badge {
                Text("NEW")
            }
        }
    }
}