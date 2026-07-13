package prac.tanken.shigure.ui.subaci.feature.base.component

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.ui.font.LocalJPFont
import prac.tanken.shigure.ui.subaci.feature.base.R

@Composable
fun VoiceButton(
    voice: Voice,
    modifier: Modifier = Modifier,
    onPlay: (Voice) -> Unit = {},
    onLongPress: () -> Unit = {},
) = AdvancedButton(
    onClick = { onPlay(voice) },
    onLongPress = onLongPress,
    shape = RoundedCornerShape(16.dp),
    modifier = modifier.width(IntrinsicSize.Max)
) {
    Text(
        text = voice.label,
        fontFamily = FontFamily(Font(resId = LocalJPFont.current.fontResId)),
        modifier = Modifier.weight(1f)
    )
    if (voice.new == true) {
        Spacer(Modifier.size(4.dp))
        Badge {
            Text(
                text = stringResource(R.string.app_badge_new),
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
//    val newBadgeTextStyle = MaterialTheme.typography.labelSmall.toSpanStyle()
//    val newBadgeString = stringResource(R.string.app_badge_new)
//    val newBadgeContainerColor = MaterialTheme.colorScheme.error
//    val buttonText = buildAnnotatedString {
//        withStyle(
//            style = SpanStyle(
//                fontFamily = FontFamily(Font(resId = LocalJPFont.current.fontResId))
//            )
//        ) {
//            append(voice.label)
//        }
//        if (voice.new == true) {
//            withAnnotation(tag = "NEW_TAG", annotation = "NEW_BADGE") {
//                withStyle(style = newBadgeTextStyle) {
//                    append(newBadgeString)
//                }
//            }
//        }
//    }
//    var spanBoxes by remember { mutableStateOf<List<Rect>>(emptyList()) }
//    Text(
//        text = buttonText,
//        onTextLayout = { layoutResult ->
//            // Extract the character indices for our annotated span
//            val annotation = buttonText.getStringAnnotations(
//                tag = "NEW_TAG",
//                start = 0,
//                end = buttonText.length
//            ).firstOrNull()
//
//            annotation?.let { annotation->
//                for(i in annotation.start..annotation.end) {
//                    spanBoxes = spanBoxes.toMutableList()
//                        .apply { add(layoutResult.getBoundingBox(i)) }
//                        .toList()
//                }
//            }
//        },
//        modifier = Modifier
//            // 3. Draw structural canvas assets safely behind the text layout layer
//            .drawBehind {
//                spanBoxes.forEach { box ->
//                    drawRoundRect(
//                        color = newBadgeContainerColor,
//                        topLeft = Offset(box.left, box.top),
//                        size = Size(box.width, box.height),
//                        cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
//                    )
//                }
//            }
//    )
}