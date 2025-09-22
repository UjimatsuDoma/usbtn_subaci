package prac.tanken.shigure.ui.subaci.feature.sources.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.feature.base.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.feature.base.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.feature.base.component.VoiceButton
import prac.tanken.shigure.ui.subaci.feature.base.component.VoicesFlowRow
import prac.tanken.shigure.ui.subaci.core.data.mock.sourcesPreviewData
import prac.tanken.shigure.ui.subaci.core.data.mock.voicesPreviewData
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.ui.NotoCJKLocale
import prac.tanken.shigure.ui.subaci.core.ui.NotoSerifJP
import prac.tanken.shigure.ui.subaci.core.ui.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.WithNotoCJKTypography
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.core.ui.theme.getNotoTypography
import prac.tanken.shigure.ui.subaci.feature.sources.SourcesViewModel
import prac.tanken.shigure.ui.subaci.feature.sources.model.SourcesListItem
import prac.tanken.shigure.ui.subaci.feature.sources.model.SourcesUiState
import prac.tanken.shigure.ui.subaci.feature.sources.R as TankenR

@Composable
fun SourcesScreen(
    modifier: Modifier = Modifier,
    viewModel: SourcesViewModel,
) {
    val uiState = viewModel.uiState
    val scope = rememberCoroutineScope()
    val saveableStateHolder = rememberSaveableStateHolder()

    when (uiState.value) {
        is SourcesUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                val actualState = uiState.value as SourcesUiState.Error

                Text("Something went wrong.\n${actualState.message}")
            }
        }

        is SourcesUiState.Loaded -> {
            val actualState = uiState.value as SourcesUiState.Loaded
            val tabs = actualState.tabs

            Column {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { tabs.size }
                )
                val selectedTab by remember {
                    derivedStateOf { pagerState.currentPage }
                }

                TabRow(
                    selectedTabIndex = selectedTab,
                ) {
                    tabs.forEachIndexed { index, tab ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { scope.launch { pagerState.animateScrollToPage(index) } },
                            text = { Text(text = stringResource(tab.tabName)) }
                        )
                    }
                }
                saveableStateHolder.SaveableStateProvider(
                    key = selectedTab
                ) {
                    HorizontalPager(
                        state = pagerState,
                        pageContent = {
                            var loading by remember { mutableStateOf(false) }
                            var sources by remember {
                                mutableStateOf(emptyList<SourcesListItem>())
                            }

                            LaunchedEffect(selectedTab) {
                                loading = true
                                sources = tabs[selectedTab].sourceList
                                loading = false
                            }

                            if (loading) LoadingScreenBody()
                            else SourcesScreen(
                                sources = sources,
                                onPlay = viewModel::playByReference,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .weight(1f)
                            )
                        }
                    )
                }

            }
        }

        SourcesUiState.Loading -> {
            Column {
                LoadingTopBar()
                LoadingScreenBody(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }

        SourcesUiState.StandBy -> {}
    }
}

@Composable
private fun SourcesScreen(
    modifier: Modifier = Modifier,
    sources: List<SourcesListItem>,
    onPlay: (VoiceReference) -> Unit = {},
) {
    LazyColumn(modifier) {
        items(sources) { source ->
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
    modifier: Modifier = Modifier,
    item: SourcesListItem,
    onPlay: (VoiceReference) -> Unit = {},
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
            WithNotoCJKTypography(NotoStyle.SERIF, NotoCJKLocale.JP) {
                Text(
                    text = item.title,
                    modifier = Modifier.weight(1f)
                )
            }
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
            WithNotoCJKTypography(NotoStyle.SANS, NotoCJKLocale.JP) {
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
}

@Preview
@Composable
private fun SourcesListItemPreview(
    modifier: Modifier = Modifier
) = ShigureUiButtonAppComposeImplementationTheme(
    typography = getNotoTypography(NotoSerifJP)
) {
    val source = sourcesPreviewData().random()
    val voices = voicesPreviewData().filter {
        it.videoId == source.videoId
    }.toList()

    SourcesListItem(
        item = SourcesListItem(source.videoId, source.title, voices),
        modifier = modifier
    )
}