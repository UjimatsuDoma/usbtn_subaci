package prac.tanken.shigure.ui.subaci.ui.sources

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import coil3.compose.SubcomposeAsyncImage
import prac.tanken.shigure.ui.subaci.R as TankenR
import prac.tanken.shigure.ui.subaci.data.mock.sourcesPreviewData
import prac.tanken.shigure.ui.subaci.data.mock.voicesPreviewData
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.ui.NotoSerifJP
import prac.tanken.shigure.ui.subaci.ui.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.ui.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.ui.component.VoiceButton
import prac.tanken.shigure.ui.subaci.ui.component.VoicesFlowRow
import prac.tanken.shigure.ui.subaci.ui.sources.model.SourcesListItem
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme

@Composable
fun SourcesScreen(
    modifier: Modifier = Modifier,
    viewModel: SourcesViewModel,
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Column(modifier) {
        if (isLoading) {
            LoadingTopBar()
            LoadingScreenBody(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            val sources by viewModel.sources

            SourcesScreen(
                sources = sources,
                onPlay = viewModel::playByReference,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@Composable
private fun SourcesScreen(
    sources: List<SourcesListItem>,
    onPlay: (VoiceReference) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollState
    ) {
        items(
            items = sources
        ) { source ->
            SourcesListItem(
                item = source,
                onPlay = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun SourcesListItem(
    item: SourcesListItem,
    onPlay: (VoiceReference) -> Unit = {},
    modifier: Modifier = Modifier
) = Card(modifier) {
    Column {
        var expanded by rememberSaveable { mutableStateOf(false) }

        val modifier = { aspectRatio: Float ->
            Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        }
        val hasVoices = item.voices.isNotEmpty()
        val isPreview = LocalInspectionMode.current

        if (isPreview) {
            Image(
                painter = painterResource(TankenR.drawable.demo_source_thumb),
                contentDescription = null,
                modifier = modifier(16f / 9f)
            )
        } else {
            SubcomposeAsyncImage(
                model = item.url,
                loading = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier(item.thumbAspectRatio),
                        content = { CircularProgressIndicator() }
                    )
                },
                contentDescription = null,
                modifier = modifier(item.thumbAspectRatio)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    if (hasVoices) {
                        expanded = !expanded
                    }
                }
                .padding(16.dp)
        ) {
            Text(
                text = item.title,
                fontFamily = NotoSerifJP,
                modifier = Modifier.weight(1f)
            )
            if (hasVoices) {
                Icon(
                    imageVector = if (!expanded) {
                        Icons.Default.ArrowDropDown
                    } else {
                        Icons.Default.ArrowDropUp
                    },
                    contentDescription = null
                )
            }
        }
        if (expanded && hasVoices) {
            VoicesFlowRow(
                voices = item.voices,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            ) { voice ->
                VoiceButton(
                    voice = voice,
                    onPlay = onPlay,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SourcesListItemPreview(
    modifier: Modifier = Modifier
) = ShigureUiButtonAppComposeImplementationTheme {
    val source = sourcesPreviewData().random()
    val voices = voicesPreviewData().filter {
        it.videoId == source.videoId
    }.toList()

    SourcesListItem(
        item = SourcesListItem(source.videoId, source.title, voices),
        modifier = modifier
    )
}