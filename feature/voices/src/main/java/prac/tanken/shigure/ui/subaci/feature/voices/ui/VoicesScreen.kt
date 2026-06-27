package prac.tanken.shigure.ui.subaci.feature.voices.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.core.data.model.voices.voicesGroupedByItems
import prac.tanken.shigure.ui.subaci.core.ui.font.LocalJPFont
import prac.tanken.shigure.ui.subaci.core.ui.util.combineKey
import prac.tanken.shigure.ui.subaci.feature.base.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.feature.base.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.feature.base.component.VoiceButton
import prac.tanken.shigure.ui.subaci.feature.base.model.voices.toReference
import prac.tanken.shigure.ui.subaci.feature.voices.R
import prac.tanken.shigure.ui.subaci.feature.voices.VoicesViewModel
import prac.tanken.shigure.ui.subaci.feature.voices.model.DailyVoiceUiState
import prac.tanken.shigure.ui.subaci.feature.voices.model.VoicesGrouped
import prac.tanken.shigure.ui.subaci.feature.voices.model.VoicesGroupedUiState
import prac.tanken.shigure.ui.subaci.core.common.R as CommonR
import prac.tanken.shigure.ui.subaci.feature.voices.R as TankenR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoicesScreen(
    modifier: Modifier = Modifier,
    viewModel: VoicesViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.snackbarMessage.collect {
                snackBarHostState.showSnackbar(message = it.message)
            }
        }
    }

    when (uiState.voicesGroupedUiState) {
        is VoicesGroupedUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                val actualState = uiState.voicesGroupedUiState as VoicesGroupedUiState.Error

                Text("Something went wrong.\n${actualState.message}")
            }
        }

        VoicesGroupedUiState.Loading -> {
            Column(modifier) {
                LoadingTopBar()
                LoadingScreenBody(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            }
        }

        VoicesGroupedUiState.StandBy -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Text(stringResource(TankenR.string.voices_loading_groups_screen))
            }
        }

        is VoicesGroupedUiState.Success -> {
            val scope = rememberCoroutineScope()
            val dailyVoiceUiState = uiState.dailyVoiceUiState
            val voicesGrouped =
                (uiState.voicesGroupedUiState as VoicesGroupedUiState.Success).voicesGrouped
            val dailyVoiceMessagePrefix =
                stringResource(R.string.daily_random_voice_play_prefix)
            val pleaseWaitMessage = stringResource(CommonR.string.please_wait_message)

            Scaffold(
                contentWindowInsets = WindowInsets(top = 0),
                snackbarHost = {
                    SnackbarHost(
                        hostState = snackBarHostState,
                        snackbar = { snackbarData ->
                            val dailyVoiceSnackbarVisuals =
                                snackbarData.visuals as? DailyVoiceSnackbarVisuals
                            dailyVoiceSnackbarVisuals?.let {
                                Snackbar(
                                    content = {
                                        Text(
                                            text = buildAnnotatedString {
                                                append(dailyVoiceSnackbarVisuals.message)
                                                withStyle(
                                                    style = SpanStyle(
                                                        fontFamily = FontFamily(
                                                            Font(resId = LocalJPFont.current.fontResId)
                                                        )
                                                    )
                                                ) {
                                                    append(dailyVoiceSnackbarVisuals.voice.label)
                                                }
                                            },
                                            modifier = Modifier.basicMarquee()
                                        )
                                    },
                                    modifier = Modifier.padding(12.dp)
                                )
                            } ?: run {
                                Snackbar(snackbarData)
                            }
                        }
                    )
                },
                topBar = {
                    VoicesTopBar(
                        dailyVoiceUiState = dailyVoiceUiState,
                        onDailyVoice = {
                            if(dailyVoiceUiState is DailyVoiceUiState.Loaded) {
                                val dailyVoice = dailyVoiceUiState.voice
                                viewModel.playDailyVoice()
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        visuals = DailyVoiceSnackbarVisuals(
                                            actionLabel = null,
                                            duration = SnackbarDuration.Long,
                                            withDismissAction = false,
                                            voice = dailyVoice,
                                            message = dailyVoiceMessagePrefix,
                                        )
                                    )
                                }
                            } else {
                                scope.launch {
                                    snackBarHostState.showSnackbar(
                                        message = pleaseWaitMessage
                                    )
                                }
                            }
                        },
                        voicesGroupedBy = voicesGrouped.voicesGroupedBy,
                        onChangeVoicesGroupedBy = viewModel::updateVoicesGroupedBy,
                    )
                },
                modifier = modifier
            ) { innerPadding ->
                VoicesScreen(
                    voicesGrouped = voicesGrouped,
                    onPlay = viewModel::onButtonClicked,
                    onAddToPlaylist = viewModel::addToPlaylist,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VoicesTopBar(
    modifier: Modifier = Modifier,
    dailyVoiceUiState: DailyVoiceUiState,
    onDailyVoice: () -> Unit,
    voicesGroupedBy: VoicesGroupedBy?,
    onChangeVoicesGroupedBy: (VoicesGroupedBy) -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(CommonR.string.app_name),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
            )
        },
        actions = {
            IconButton(
                onClick = onDailyVoice,
                enabled = dailyVoiceUiState is DailyVoiceUiState.Loaded
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = stringResource(TankenR.string.voices_desc_daily_random_voice)
                )
            }
            var showGroupedByMenu by rememberSaveable { mutableStateOf(false) }

            IconButton(onClick = { showGroupedByMenu = !showGroupedByMenu }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.Sort,
                    contentDescription = null
                )
            }
            DropdownMenu(
                expanded = showGroupedByMenu,
                onDismissRequest = { showGroupedByMenu = false },
                modifier = Modifier.selectableGroup()
            ) {
                voicesGroupedByItems.forEach {
                    val selected = voicesGroupedBy == it
                    val onSelect = {
                        showGroupedByMenu = false
                        onChangeVoicesGroupedBy(it)
                    }

                    DropdownMenuItem(
                        text = {
                            Text(
                                text = stringResource(it.displayName),
                                style = MaterialTheme.typography.labelLarge,
                            )
                        },
                        leadingIcon = {
                            RadioButton(
                                selected = selected,
                                onClick = null
                            )
                        },
                        onClick = onSelect,
                        modifier = Modifier
                            .selectable(
                                selected = selected,
                                onClick = onSelect,
                                role = Role.RadioButton
                            )
                    )
                }
            }
        }
    )
}

@Composable
private fun VoicesScreen(
    modifier: Modifier = Modifier,
    voicesGrouped: VoicesGrouped?,
    onPlay: (VoiceReference) -> Unit,
    onAddToPlaylist: (VoiceReference) -> Unit,
) {
    val scope: CoroutineScope = rememberCoroutineScope()
    val lazyStaggeredGridState = rememberLazyStaggeredGridState()

    val mapEntries = voicesGrouped?.voiceGroups?.entries?.toList() ?: emptyList()
    val headerTitleIndices = mutableListOf<Pair<String, Int>>().apply {
        mapEntries.forEachIndexed { index, (title, _) ->
            add(title to (if (index == 0) 0 else this[index - 1].second + mapEntries[index - 1].value.size + 1))
        }
    }
    var showVoiceGroupDialog by remember { mutableStateOf(false) }
    if (showVoiceGroupDialog) {
        VoiceGroupDialog(
            items = headerTitleIndices,
            onItemClicked = {
                scope.launch {
                    val index = headerTitleIndices[it].second
                    lazyStaggeredGridState.scrollToItem(index)
                }
            },
            onDismiss = { showVoiceGroupDialog = false },
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .fillMaxHeight(0.5f)
        )
    }


    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = lazyStaggeredGridState,
        verticalItemSpacing = 4.dp,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier.padding(horizontal = 4.dp)
    ) {
        mapEntries.forEach { (title, voicesVOs) ->
            item(span = StaggeredGridItemSpan.FullLine) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { showVoiceGroupDialog = true }
                        .fillMaxWidth()
                        .padding(8.dp)
                )
            }
            items(items = voicesVOs, key = { it.id }) { voicesVO ->
                var expanded by remember { mutableStateOf(false) }

                Column {
                    VoiceButton(
                        voicesVO = voicesVO,
                        onPlay = onPlay,
                        onLongPress = { expanded = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(TankenR.string.voices_add_to_playlist)) },
                            onClick = {
                                expanded = false
                                onAddToPlaylist(voicesVO.toReference())
                            }
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun VoiceGroupDialog(
    modifier: Modifier = Modifier,
    items: List<Pair<String, Int>>,
    onItemClicked: (Int) -> Unit = {},
    onDismiss: () -> Unit = {},
) = Dialog(onDismiss) {
    Card(modifier) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
            content = {
                Text(
                    text = stringResource(TankenR.string.voices_group_dialog_title),
                    style = MaterialTheme.typography.headlineMedium,
                )
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(
                        items = items,
                        key = { index, item -> index combineKey item }
                    ) { index, item ->
                        Text(
                            text = item.first,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .clickable {
                                    onDismiss()
                                    onItemClicked(index)
                                }
                                .fillMaxWidth()
                        )
                    }
                }
            }
        )
    }
}

data class DailyVoiceSnackbarVisuals(
    override val actionLabel: String?,
    override val duration: SnackbarDuration,
    override val message: String,
    override val withDismissAction: Boolean,
    val voice: Voice,
): SnackbarVisuals