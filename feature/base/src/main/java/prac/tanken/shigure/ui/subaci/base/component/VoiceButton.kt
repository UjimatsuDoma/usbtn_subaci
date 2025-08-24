package prac.tanken.shigure.ui.subaci.base.component

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
import prac.tanken.shigure.ui.subaci.base.R
import prac.tanken.shigure.ui.subaci.base.model.voice.toReference
import prac.tanken.shigure.ui.subaci.base.model.voice.toVoicesVO
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference
import prac.tanken.shigure.ui.subaci.core.ui.NotoCJKLocale
import prac.tanken.shigure.ui.subaci.core.ui.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.WithNotoCJKTypography

@Composable
fun VoiceButton(
    voice: Voice,
    modifier: Modifier = Modifier,
    onPlay: (VoiceReference) -> Unit = {},
    onLongPress: ()->Unit = {},
) = AdvancedButton(
    onClick = { onPlay(voice.toVoicesVO().toReference()) },
    onLongPress = onLongPress,
    modifier = modifier.width(IntrinsicSize.Max)
) {
    Text(
        text = voice.label,
        modifier = Modifier.weight(1f)
    )
    if (voice.new == true) {
        Spacer(Modifier.size(4.dp))
        WithNotoCJKTypography(NotoStyle.SANS) {
            Badge { Text(stringResource(R.string.app_badge_new)) }
        }
    }
}