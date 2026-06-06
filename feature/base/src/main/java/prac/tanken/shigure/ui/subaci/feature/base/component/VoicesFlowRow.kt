@file:OptIn(ExperimentalLayoutApi::class)

package prac.tanken.shigure.ui.subaci.feature.base.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.core.data.mock.voicesPreviewData
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.VoicesVO
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.toVoicesVO
import kotlin.random.Random
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
fun VoicesFlowRow(
    voices: List<VoicesVO>,
    modifier: Modifier = Modifier,
    elementContent: @Composable (VoicesVO) -> Unit = {}
) {
    val state = rememberLazyStaggeredGridState()

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = state,
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
    ) {
        items(items = voices, key = { voice -> voice.id }) { voice ->
            elementContent(voice)
        }
    }
}

@OptIn(ExperimentalTime::class)
@Preview
@Composable
private fun VoicesFlowRowPreview() = MaterialTheme {
    val voices = voicesPreviewData()
        .shuffled()
        .take(Random(Clock.System.now().nanosecondsOfSecond).nextInt(50, 100))
        .map { it.toVoicesVO() }

    VoicesFlowRow(voices) { voicesVO ->
        VoiceButton(voicesVO)
    }
}