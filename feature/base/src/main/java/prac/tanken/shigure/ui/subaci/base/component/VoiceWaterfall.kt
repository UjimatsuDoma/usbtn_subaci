package prac.tanken.shigure.ui.subaci.base.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.core.common.collection.randomList
import prac.tanken.shigure.ui.subaci.core.data.mock.voicesPreviewData
import prac.tanken.shigure.ui.subaci.core.data.model.voice.Voice
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme

@Composable
fun VoiceWaterfall(
    voices: List<Voice>,
    modifier: Modifier = Modifier,
//    pageSize: Int = 20,
    elementContent: @Composable (Voice) -> Unit = {}
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
private fun VoiceWaterfallPreviewLesser() = ShigureUiButtonAppComposeImplementationTheme {
    val voices = voicesPreviewData().randomList(15)

    VoiceWaterfall(voices) {
        VoiceButton(it)
    }
}

@Preview
@Composable
private fun VoiceWaterfallPreview() = ShigureUiButtonAppComposeImplementationTheme {
    val voices = voicesPreviewData().randomList(55)

    VoiceWaterfall(voices) {
        VoiceButton(it)
    }
}