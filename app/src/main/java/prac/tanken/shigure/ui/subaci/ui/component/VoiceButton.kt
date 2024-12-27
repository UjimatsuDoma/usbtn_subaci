package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.data.util.CallbackInvokedAsIs
import prac.tanken.shigure.ui.subaci.ui.NotoSansJP

@Composable
fun VoiceButton(
    voice: Voice,
    modifier: Modifier = Modifier,
    onPlay: (VoiceReference) -> Unit = {},
    onLongPress: CallbackInvokedAsIs = {},
) {
    AdvancedButton(
        onClick = { onPlay(voice.toReference()) },
        onLongPress = onLongPress,
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