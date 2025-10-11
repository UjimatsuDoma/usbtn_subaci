package prac.tanken.shigure.ui.subaci.feature.base.component

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.ui.NotoSansAuto
import prac.tanken.shigure.ui.subaci.feature.base.R
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.VoicesVO
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.toReference

@Composable
fun VoiceButton(
    voicesVO: VoicesVO,
    modifier: Modifier = Modifier,
    onPlay: (VoiceReference) -> Unit = {},
    onLongPress: () -> Unit = {},
) = AdvancedButton(
    onClick = { onPlay(voicesVO.toReference()) },
    onLongPress = onLongPress,
    shape = RoundedCornerShape(16.dp),
    modifier = modifier.width(IntrinsicSize.Max)
) {
    Text(
        text = voicesVO.label,
        modifier = Modifier.weight(1f)
    )
    if (voicesVO.new) {
        Spacer(Modifier.size(4.dp))
        NotoSansAuto {
            Badge { Text(stringResource(R.string.app_badge_new)) }
        }
    }
}