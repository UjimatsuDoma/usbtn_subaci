package prac.tanken.shigure.ui.subaci.feature.sources.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import com.drew.imaging.ImageMetadataReader
import com.drew.metadata.jpeg.JpegDirectory
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.mock.sourcesPreviewData
import prac.tanken.shigure.ui.subaci.core.data.mock.voicesPreviewData
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.sources.SourceEntity
import prac.tanken.shigure.ui.subaci.core.ui.font.LocalJPFont
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.getNotoFamilyByLocalesNonComposable
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.core.ui.theme.getTypographyByFontFamily
import prac.tanken.shigure.ui.subaci.feature.base.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.feature.base.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.feature.base.component.VoiceButton
import prac.tanken.shigure.ui.subaci.feature.base.component.VoicesFlowRow
import prac.tanken.shigure.ui.subaci.feature.sources.SourcesContract
import prac.tanken.shigure.ui.subaci.feature.sources.SourcesViewModel
import prac.tanken.shigure.ui.subaci.feature.sources.model.SourcesListItem
import prac.tanken.shigure.ui.subaci.feature.sources.model.SourcesUiState
import java.io.InputStream
import prac.tanken.shigure.ui.subaci.feature.sources.R as TankenR

@Composable
fun SourcesScreen(
    modifier: Modifier = Modifier,
    viewModel: SourcesViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val saveableStateHolder = rememberSaveableStateHolder()

    when (val uiState = state.sourcesUiState) {
        is SourcesContract.SourcesUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text("Something went wrong.\n${uiState.message}")
            }
        }

        is SourcesContract.SourcesUiState.Loaded -> {
            Column {
                val pagerState = rememberPagerState(
                    initialPage = 0,
                    pageCount = { 2 }
                )
                val selectedTab by remember {
                    derivedStateOf { pagerState.currentPage }
                }

                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = { scope.launch { pagerState.animateScrollToPage(0) } },
                        text = { Text(text = stringResource(TankenR.string.sources_tab_with_voices)) }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = { scope.launch { pagerState.animateScrollToPage(1) } },
                        text = { Text(text = stringResource(TankenR.string.sources_tab_without_voices)) }
                    )
                }
                saveableStateHolder.SaveableStateProvider(
                    key = selectedTab
                ) {
                    HorizontalPager(
                        state = pagerState,
                        pageContent = {
                            var loading by remember { mutableStateOf(false) }
                            var sources by remember {
                                mutableStateOf(emptyMap<SourceEntity, List<Voice>>())
                            }

                            LaunchedEffect(selectedTab) {
                                loading = true
                                sources = when (selectedTab) {
                                    0 -> uiState.sourcesWithVoice.voiceGroups
                                    1 -> uiState.sourcesWithoutVoice.associateWith { emptyList() }
                                    else -> emptyMap()
                                }
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

        SourcesContract.SourcesUiState.Loading -> {
            Column {
                LoadingTopBar()
                LoadingScreenBody(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }
    }
}

@Composable
private fun SourcesScreen(
    modifier: Modifier = Modifier,
    sources: Map<SourceEntity, List<Voice>>,
    onPlay: (Voice) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(sources.toList()) { source ->
            SourcesListItem(
                item = source,
                onPlay = onPlay,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SourcesListItem(
    modifier: Modifier = Modifier,
    item: Pair<SourceEntity, List<Voice>>,
    onPlay: (Voice) -> Unit = {},
) = Card(modifier) {
    val videoId = item.first.videoId
    val url = "file:///android_asset/subaciThumbs/$videoId.jpg"
    val imageIs = LocalContext.current
        .assets.openFd("subaciThumbs/$videoId.jpg")
        .createInputStream()
    val thumbAspectRatio = ImageMetadataReader.readMetadata(imageIs)
        .getFirstDirectoryOfType(JpegDirectory::class.java)
        .run { imageWidth.toFloat() / imageHeight.toFloat() }

    Column {
        var expanded by rememberSaveable { mutableStateOf(false) }

        val modifier = { aspectRatio: Float ->
            Modifier
                .fillMaxWidth()
                .aspectRatio(aspectRatio)
        }
        val hasVoices = item.second.isNotEmpty()
        val isPreview = LocalInspectionMode.current

        if (isPreview) {
            Image(
                painter = painterResource(TankenR.drawable.demo_source_thumb),
                contentDescription = null,
                modifier = modifier(16f / 9f)
            )
        } else {
            SubcomposeAsyncImage(
                model = url,
                loading = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = modifier(thumbAspectRatio),
                        content = { CircularProgressIndicator() }
                    )
                },
                contentDescription = null,
                modifier = modifier(thumbAspectRatio)
            )
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    if (hasVoices) {
                        expanded = true
                    }
                }
                .padding(16.dp)
        ) {
            Text(
                text = item.first.title,
                modifier = Modifier.weight(1f),
                fontFamily = FontFamily(Font(LocalJPFont.current.fontResId))
            )
            if (hasVoices) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowRight,
                    contentDescription = null
                )
            }
        }
        if (expanded && hasVoices) {
            ModalBottomSheet(onDismissRequest = { expanded = false }) {
                Column {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                            .padding(8.dp)
                    ) {
                        SubcomposeAsyncImage(
                            model = url,
                            loading = {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = modifier(thumbAspectRatio),
                                    content = { CircularProgressIndicator() }
                                )
                            },
                            contentDescription = null,
                            modifier = modifier(thumbAspectRatio)
                        )
                    }
                    Text(
                        text = item.first.title,
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(8.dp),
                        fontFamily = FontFamily(Font(LocalJPFont.current.fontResId))
                    )
                    VoicesFlowRow(
                        voices = item.second,
                        modifier = Modifier
                            .padding(8.dp),
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
}

@Preview
@Composable
private fun SourcesListItemPreview(
    modifier: Modifier = Modifier
) = ShigureUiButtonAppComposeImplementationTheme(
    typography = getTypographyByFontFamily(
        getNotoFamilyByLocalesNonComposable(
            LocalContext.current, NotoStyle.SERIF
        )
    )
) {
    val source = sourcesPreviewData().random()
    val voices = voicesPreviewData().filter {
        it.videoId == source.videoId
    }.toList()

    SourcesListItem(
        item = source to voices,
        modifier = modifier
    )
}