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
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.base.R
import prac.tanken.shigure.ui.subaci.base.model.voice.VoicesVO
import prac.tanken.shigure.ui.subaci.base.model.voice.toReference
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voice.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.model.voice.toReference
import prac.tanken.shigure.ui.subaci.core.ui.NotoSansJP

@Composable
fun VoiceButton(
    voice: Voice,
    modifier: Modifier = Modifier,
    onPlay: (VoiceReference) -> Unit = {},
    onLongPress: ()->Unit = {},
) = AdvancedButton(
    onClick = { onPlay(voice.toReference()) },
    onLongPress = onLongPress,
    contentFontFamily = NotoSansJP,
//    contentFontFamily = FontFamily.SansSerif,
    modifier = modifier.width(IntrinsicSize.Max)
) {
    Text(
        text = voice.label,
        modifier = Modifier.weight(1f)
    )
    if (voice.new == true) {
        Spacer(Modifier.size(4.dp))
        Badge { Text(stringResource(R.string.app_badge_new)) }
    }
}

@Composable
fun TestVoiceButton(
    vo: VoicesVO,
    modifier: Modifier = Modifier,
    onPlay: (VoiceReference) -> Unit = {},
    onLongPress: ()->Unit = {},
) = AdvancedButton(
    onClick = { onPlay(vo.toReference()) },
    onLongPress = onLongPress,
    contentFontFamily = NotoSansJP,
//    contentFontFamily = FontFamily.SansSerif,
    modifier = modifier.width(IntrinsicSize.Max)
) {
    Text(
        text = vo.label,
        modifier = Modifier.weight(1f)
    )
    if (vo.new) {
        Spacer(Modifier.size(4.dp))
        Badge { Text(stringResource(R.string.app_badge_new)) }
    }
}