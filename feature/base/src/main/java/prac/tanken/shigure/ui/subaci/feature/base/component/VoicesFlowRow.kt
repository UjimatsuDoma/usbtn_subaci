@file:OptIn(ExperimentalLayoutApi::class)

package prac.tanken.shigure.ui.subaci.feature.base.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.core.data.mock.voicesPreviewData
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.VoicesVO
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.toVoicesVO

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
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(items = voices, key = { voice -> voice.id }) { voice ->
            elementContent(voice)
        }
    }
}

@Preview
@Composable
private fun VoicesFlowRowPreview() = ShigureUiButtonAppComposeImplementationTheme {
    val voices = voicesPreviewData().shuffled().subList(0, 55).map { it.toVoicesVO() }

    VoicesFlowRow(voices){ voicesVO ->
        VoiceButton(voicesVO)
    }
}