package prac.tanken.shigure.ui.subaci.ui.voices

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.BuildConfig
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceGroup
import prac.tanken.shigure.ui.subaci.data.util.CallbackInvokedAsIs
import prac.tanken.shigure.ui.subaci.ui.NotoSerifJP
import prac.tanken.shigure.ui.subaci.ui.NotoSerifMultiLang
import prac.tanken.shigure.ui.subaci.ui.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.ui.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.ui.component.VoiceButton
import prac.tanken.shigure.ui.subaci.ui.component.VoicesFlowRow
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGrouped
import prac.tanken.shigure.ui.subaci.data.model.voices.VoicesGroupedBy
import prac.tanken.shigure.ui.subaci.data.model.voices.voicesGroupedByItems
import prac.tanken.shigure.ui.subaci.data.util.combineKey
import prac.tanken.shigure.ui.subaci.R as TankenR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoicesScreen(
    modifier: Modifier = Modifier,
    viewModel: VoicesViewModel,
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
            var test by remember { mutableStateOf(false) }
            val dailyVoiceUiState by viewModel.dailyVoiceUiState
            val voicesGrouped by viewModel.voicesGrouped

            VoicesTopBar(
                dailyVoiceUiState = dailyVoiceUiState,
                onDailyVoice = viewModel::playDailyVoice,
                test = { test = true },
                voicesGroupedBy = voicesGrouped?.voicesGroupedBy,
                onChangeVoicesGroupedBy = viewModel::updateVoicesGroupedBy,
            )
            VoicesScreen(
                voicesGrouped = voicesGrouped,
                onPlay = viewModel::onButtonClicked,
                onAddToPlaylist = viewModel::addToPlaylist,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
            if (test) {
                TestModal(testDismiss = { test = false })
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
    test: CallbackInvokedAsIs = {},
    voicesGroupedBy: VoicesGroupedBy?,
    onChangeVoicesGroupedBy: (VoicesGroupedBy) -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(TankenR.string.app_name),
                fontWeight = FontWeight.Black,
                fontFamily = NotoSerifMultiLang
            )
        },
        actions = {
            IconButton(
                onClick = onDailyVoice,
                enabled = dailyVoiceUiState is DailyVoiceUiState.Loaded
            ) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = stringResource(TankenR.string.daily_random_voice)
                )
            }
            IconButton(onClick = test) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
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
                                style = MaterialTheme.typography.bodyLarge,
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
    voicesGrouped: VoicesGrouped?,
    onPlay: (VoiceReference) -> Unit,
    onAddToPlaylist: (VoiceReference) -> Unit,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberLazyListState()

    LazyColumn(
        modifier = modifier,
        state = scrollState
    ) {
        val list = voicesGrouped?.voiceGroups?.entries?.toList() ?: emptyList()

        items(items = list) { (name, voices): VoiceGroup ->
            Column {
                var showVoiceGroupDialog by remember { mutableStateOf(false) }

                Text(
                    text = name,
                    fontFamily = NotoSerifJP,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier
                        .clickable { showVoiceGroupDialog = true }
                        .fillMaxWidth()
                        .padding(16.dp)
                )
                VoicesFlowRow(voices) { voice ->
                    var expanded by remember { mutableStateOf(false) }

                    Column {
                        VoiceButton(
                            voice = voice,
                            onPlay = onPlay,
                            onLongPress = { expanded = true },
                        )
                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text(stringResource(TankenR.string.voices_add_to_playlist)) },
                                onClick = {
                                    expanded = false
                                    onAddToPlaylist(voice.toReference())
                                }
                            )
                        }
                    }
                }

                if (showVoiceGroupDialog) {
                    VoiceGroupDialog(
                        title = "Select",
                        items = list.map { it.key },
                        onItemClicked = { scope.launch { scrollState.scrollToItem(index = it) } },
                        onDismiss = { showVoiceGroupDialog = false },
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .fillMaxHeight(0.5f)
                    )
                }
            }
        }
    }
}

@Composable
fun VoiceGroupDialog(
    title: String,
    items: List<String>,
    onItemClicked: (Int) -> Unit = {},
    onDismiss: CallbackInvokedAsIs = {},
    modifier: Modifier = Modifier
) = Dialog(onDismiss) {
    Card(modifier) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp),
            content = {
                Text(
                    text = title,
                    fontSize = 32.sp,
                    style = MaterialTheme.typography.titleLarge
                )
                LazyColumn(Modifier.weight(1f)) {
                    itemsIndexed(
                        items = items,
                        key = { index, item -> index combineKey item }
                    ) { index, item ->
                        Text(
                            text = item,
                            fontFamily = NotoSerifJP,
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            modifier = Modifier
                                .clickable {
                                    onDismiss()
                                    onItemClicked(index)
                                }
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 16.dp,
                                    vertical = 8.dp
                                )
                        )
                    }
                }
            }
        )
    }
}

@Composable
private fun TestDialog(
    modifier: Modifier = Modifier,
    testDismiss: CallbackInvokedAsIs,
) {
    Dialog(
        onDismissRequest = testDismiss,
    ) {
        Card(
            modifier = modifier
                .fillMaxHeight(0.5f),
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = BuildConfig.VERSION_NAME,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TestModal(
    modifier: Modifier = Modifier,
    testDismiss: CallbackInvokedAsIs,
) {
    ModalBottomSheet(
        onDismissRequest = testDismiss
    ) {
        Box(
            modifier = modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = BuildConfig.VERSION_NAME,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
