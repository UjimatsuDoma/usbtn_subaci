package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ContextualFlowRow
import androidx.compose.foundation.layout.ContextualFlowRowOverflow
import androidx.compose.foundation.layout.ContextualFlowRowOverflowScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import com.microsoft.fluent.mobile.icons.R as FluentR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun VoicesFlowRow(
    voices: List<Voice>,
    modifier: Modifier = Modifier,
    initialMaxLines: Int = 10,
    linesDeltaStep: Int = 5,
    onButtonClicked: (VoiceReference) -> Unit = {},
) {
    val totalItems = voices.size
    var linesShown by rememberSaveable(voices) { mutableIntStateOf(initialMaxLines) }

    val moreOrLessIndicator = @Composable { scope: ContextualFlowRowOverflowScope ->
        val remainingItems = totalItems - scope.shownItemCount

        SingleChoiceSegmentedButtonRow {
            val showPlus = (remainingItems > 0)
            val showMinus = (linesShown > initialMaxLines)
            val minusIndex = if (showPlus) 1 else 0
            val count = if (showPlus && showMinus) 2 else 1

            if (showPlus) {
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = 0, count = count),
                    selected = false,
                    onClick = { linesShown += linesDeltaStep }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_add_24_regular),
                        contentDescription = null
                    )
                }
            }
            if (showMinus) {
                SegmentedButton(
                    shape = SegmentedButtonDefaults.itemShape(index = minusIndex, count = count),
                    selected = false,
                    enabled = (linesShown > initialMaxLines),
                    onClick = { linesShown -= linesDeltaStep }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_subtract_24_regular),
                        contentDescription = null
                    )
                }
            }
        }
    }

    ContextualFlowRow(
        modifier = modifier
            .padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        maxLines = linesShown,
        overflow = ContextualFlowRowOverflow.expandOrCollapseIndicator(
            minRowsToShowCollapse = initialMaxLines,
            expandIndicator = moreOrLessIndicator,
            collapseIndicator = moreOrLessIndicator,
        ),
        itemCount = totalItems
    ) {
        voices.forEachIndexed { index, voice ->
            AdvancedButton(
                onClick = { onButtonClicked(voice.toReference()) }
            ) {
                Row {
                    Text(text = voice.label)
                    if (voice.new == true) {
                        Badge {
                            Text("NEW")
                        }
                    }
                }
            }
        }
    }
}