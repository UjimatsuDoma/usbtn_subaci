package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.R as TankenR
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.model.voices.toReference
import prac.tanken.shigure.ui.subaci.data.util.CallbackInvokedAsIs
import prac.tanken.shigure.ui.subaci.ui.NotoSansJP

@Composable
fun VoiceButton(
    voice: Voice,
    modifier: Modifier = Modifier,
    onPlay: (VoiceReference) -> Unit = {},
    onLongPress: CallbackInvokedAsIs = {},
) = AdvancedButton(
    onClick = { onPlay(voice.toReference()) },
    onLongPress = onLongPress,
    contentFontFamily = NotoSansJP,
    modifier = modifier.width(IntrinsicSize.Max)
) {
    Text(
        text = voice.label,
        modifier = Modifier.weight(1f)
    )
    if (voice.new == true) {
        Spacer(Modifier.size(4.dp))
        Badge { Text(stringResource(TankenR.string.app_badge_new)) }
    }
}