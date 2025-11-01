package prac.tanken.shigure.ui.subaci.feature.playlist.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prac.tanken.shigure.ui.subaci.feature.base.component.ErrorMessageStrip
import prac.tanken.shigure.ui.subaci.feature.base.component.InfoMessageStrip
import prac.tanken.shigure.ui.subaci.core.ui.NotoCJKLocale
import prac.tanken.shigure.ui.subaci.core.ui.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.WithNotoCJKTypography
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.core.ui.util.combineKey
import prac.tanken.shigure.ui.subaci.feature.playlist.PlaylistViewModel
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistPlaybackIntent
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistPlaybackSettings
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistPlaybackState
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistSelectionVO
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertError
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertIntent
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertState
import prac.tanken.shigure.ui.subaci.feature.playlist.model.playlistNotSelectedVO
import com.microsoft.fluent.mobile.icons.R as FluentR
import prac.tanken.shigure.ui.subaci.core.common.R as CommonR
import prac.tanken.shigure.ui.subaci.feature.base.R as BaseR
import prac.tanken.shigure.ui.subaci.feature.playlist.R as PlaylistR

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
) {
    val playbackState by viewModel.playbackState
    val upsertState by viewModel.upsertState
    val playlistsSelections = viewModel.playlistSelections.value
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }

    Column(modifier.fillMaxWidth()) {
        // 顶栏
        PlaylistTopBar(
            playbackState = playbackState,
            playlistSelection = playlistsSelections,
            onAddPlaylist = viewModel::showInsertDialog,
            onDeletePlaylist = { showDeleteDialog = true },
            onPlaylistSelect = viewModel::selectPlaylist,
            onShowUpdateDialog = viewModel::showUpdateDialog
        )
        // 不同状态下的主体内容
        when (playbackState) {
            PlaylistPlaybackState.StandBy -> {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                    content = {
                        val text = if (playlistsSelections.isNotEmpty()) {
                            stringResource(PlaylistR.string.playlist_select_playlist_content)
                        } else {
                            stringResource(PlaylistR.string.playlist_no_playlists_content)
                        }

                        Text(
                            text = text,
                            modifier = modifier,
                            textAlign = TextAlign.Center,
                        )
                    }
                )
            }

            PlaylistPlaybackState.Error -> {
                Box { Text("ERROR") }
            }

            PlaylistPlaybackState.Loading -> {
                Box(
                    modifier = modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                    content = {
                        CircularProgressIndicator()
                    }
                )
            }

            is PlaylistPlaybackState.Loaded -> {
                val actualState = playbackState as PlaylistPlaybackState.Loaded
                val playbackSettings by viewModel.playbackSettings.collectAsStateWithLifecycle()

                PlaylistScreen(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    playbackState = actualState,
                    playbackSettings = playbackSettings,
                    onItemClicked = viewModel::playItem,
                    onItemMove = viewModel::movePlaylistItem,
                    onItemDelete = viewModel::removePlaylistItem,
                    onToggleLooping = viewModel::toggleLooping,
                    onPlayOrStop = viewModel::dispatchPlaybackIntent,
                )
            }
        }

        // 添加、重命名对话框
        if (upsertState is PlaylistUpsertState.Draft) {
            val state = upsertState as PlaylistUpsertState.Draft

            PlaylistUpsertDialog(
                state = state,
                onStateUpdate = viewModel::updateUpsertState,
                onSubmit = viewModel::submitUpsert,
                onCancel = viewModel::cancelUpsert
            )
        }

        if (showDeleteDialog) {
            PlaylistDeleteDialog(
                onSubmit = {
                    showDeleteDialog = false
                    viewModel::deletePlaylist
                },
                onCancel = { showDeleteDialog = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaylistTopBar(
    modifier: Modifier = Modifier,
    playlistSelection: List<PlaylistSelectionVO> = emptyList(),
    playbackState: PlaylistPlaybackState,
    onAddPlaylist: () -> Unit = {},
    onDeletePlaylist: () -> Unit = {},
    onPlaylistSelect: (Long) -> Unit = {},
    onShowUpdateDialog: (PlaylistPlaybackState.Loaded) -> Unit = {},
) {
    val hasPlaylist = !playlistSelection.isEmpty()
    val title = when (playbackState) {
        PlaylistPlaybackState.Error -> "__ERROR__"
        PlaylistPlaybackState.Loading -> stringResource(BaseR.string.loading)
        PlaylistPlaybackState.StandBy -> {
            if (!hasPlaylist) {
                stringResource(PlaylistR.string.playlist_no_playlists)
            } else {
                stringResource(PlaylistR.string.playlist_select_playlist)
            }
        }

        is PlaylistPlaybackState.Loaded -> playbackState.playlist.playlistName
    }

    var expanded by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Column {
                val canToggleSelectionMenu =
                    playbackState is PlaylistPlaybackState.Loaded.Stopped && hasPlaylist

                Row(
                    modifier = Modifier
                        .clickable {
                            if (canToggleSelectionMenu) {
                                expanded = !expanded
                            }
                        },
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val trailingIcon =
                        if (expanded) {
                            painterResource(FluentR.drawable.ic_fluent_caret_up_24_filled)
                        } else {
                            painterResource(FluentR.drawable.ic_fluent_caret_down_24_filled)
                        }

                    WithNotoCJKTypography(NotoStyle.SERIF) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.basicMarquee()
                        )
                        if (canToggleSelectionMenu) {
                            Spacer(Modifier.width(8.dp))
                            Icon(
                                painter = trailingIcon,
                                contentDescription = null
                            )
                        }
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(200.dp)
                ) {
                    if (hasPlaylist) {
                        playlistSelection.forEach { selection ->
                            DropdownMenuItem(
                                text = { Text(selection.playlistName) },
                                onClick = {
                                    onPlaylistSelect(selection.id)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        actions = {
            if (playbackState is PlaylistPlaybackState.Loaded.Stopped) {
                IconButton(
                    onClick = { onAddPlaylist() }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_add_24_filled),
                        contentDescription = stringResource(PlaylistR.string.playlist_desc_add_playlist)
                    )
                }
                IconButton(
                    onClick = { onDeletePlaylist() }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_bin_recycle_24_filled),
                        contentDescription = stringResource(PlaylistR.string.playlist_desc_delete_playlist)
                    )
                }
                IconButton(
                    onClick = { onShowUpdateDialog(playbackState) }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_rename_24_filled),
                        contentDescription = stringResource(PlaylistR.string.playlist_desc_rename_playlist)
                    )
                }
            } else if (playbackState is PlaylistPlaybackState.StandBy && !hasPlaylist) {
                IconButton(
                    onClick = { onAddPlaylist() }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_add_24_filled),
                        contentDescription = stringResource(PlaylistR.string.playlist_desc_add_playlist)
                    )
                }
            }
        },
        modifier = modifier
    )
}

@Preview
@Composable
private fun PlaylistTopBarPreview() {
    ShigureUiButtonAppComposeImplementationTheme {
        PlaylistTopBar(
            playbackState = PlaylistPlaybackState.Loaded.Stopped(playlistNotSelectedVO)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlaylistScreen(
    modifier: Modifier = Modifier,
    playbackState: PlaylistPlaybackState.Loaded,
    playbackSettings: PlaylistPlaybackSettings,
    onItemClicked: (Int) -> Unit,
    onItemMove: (Int, Boolean) -> Unit,
    onItemDelete: (Int) -> Unit,
    onToggleLooping: () -> Unit = {},
    onPlayOrStop: (PlaylistPlaybackIntent) -> Unit = {},
) {
    val lazyListState = rememberLazyListState()
    val canPlay = playbackState.playlistHasItems()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = {
            if (playbackState.playlistHasItems()) {
                val voices = playbackState.playlist.voices

                LazyColumn(
                    modifier = modifier.fillMaxSize(),
                    state = lazyListState,
                    horizontalAlignment = Alignment.Start,
                ) {
                    itemsIndexed(
                        items = voices,
                        key = { index, voice -> index combineKey voice }
                    ) { index, voice ->
                        Column {
                            var expanded by rememberSaveable { mutableStateOf(false) }

                            WithNotoCJKTypography(NotoStyle.SERIF, NotoCJKLocale.JP) {
                                Text(
                                    text = voice.label,
                                    fontSize = 24.sp,
                                    fontWeight = when (playbackState) {
                                        is PlaylistPlaybackState.Loaded.Playing -> {
                                            if (playbackState.index == index) FontWeight.ExtraBold
                                            else FontWeight.Normal
                                        }

                                        is PlaylistPlaybackState.Loaded.Stopped -> FontWeight.Normal
                                    },
                                    modifier = Modifier
                                        .combinedClickable(
                                            onClick = { onItemClicked(index) },
                                            onLongClick = {
                                                if (playbackState !is PlaylistPlaybackState.Loaded.Playing)
                                                    expanded = true
                                            }
                                        )
                                        .fillMaxWidth()
                                        .padding(16.dp)
                                )
                            }

                            DropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text(stringResource(PlaylistR.string.playlist_move_up)) },
                                    enabled = index in 1..voices.lastIndex,
                                    onClick = {
                                        expanded = false
                                        onItemMove(index, true)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(PlaylistR.string.playlist_move_down)) },
                                    enabled = index in 0 until voices.lastIndex,
                                    onClick = {
                                        expanded = false
                                        onItemMove(index, false)
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text(stringResource(PlaylistR.string.playlist_delete_item)) },
                                    onClick = {
                                        expanded = false
                                        onItemDelete(index)
                                    }
                                )
                            }
                        }

                        if (index in 0 until voices.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(2.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    stringResource(PlaylistR.string.playlist_no_item),
                    Modifier.fillMaxWidth(0.7f),
                    textAlign = TextAlign.Center,
                )
            }

            AnimatedVisibility(
                visible = canPlay,
                modifier = Modifier
                    .align(Alignment.BottomEnd),
                enter = slideInHorizontally(initialOffsetX = { it * 2 }),
                exit = slideOutHorizontally(targetOffsetX = { it * 2 }),
                content = {
                    PlaylistFloatingActionButton(
                        playbackState = playbackState,
                        looping = playbackSettings.looping,
                        onToggleLooping = onToggleLooping,
                        onPlayOrStop = onPlayOrStop,
                        modifier = Modifier
                            .wrapContentSize()
                            .padding(bottom = 16.dp, end = 16.dp)
                    )
                }
            )
        }
    )
}

@Composable
private fun PlaylistFloatingActionButton(
    modifier: Modifier = Modifier,
    playbackState: PlaylistPlaybackState.Loaded =
        PlaylistPlaybackState.Loaded.Stopped(playlistNotSelectedVO),
    looping: Boolean = false,
    onToggleLooping: () -> Unit = {},
    onPlayOrStop: (PlaylistPlaybackIntent) -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        val playIcon = when (playbackState) {
            is PlaylistPlaybackState.Loaded.Playing -> FluentR.drawable.ic_fluent_stop_24_filled
            is PlaylistPlaybackState.Loaded.Stopped -> FluentR.drawable.ic_fluent_play_24_filled
        }
        val loopingIcon = FluentR.drawable.ic_fluent_arrow_clockwise_24_filled
        val loopingContainerColor =
            if (looping) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
        val loopingContentColor =
            if (looping) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSecondary
        val playbackAction = when (playbackState) {
            is PlaylistPlaybackState.Loaded.Playing -> PlaylistPlaybackIntent.Stop
            is PlaylistPlaybackState.Loaded.Stopped -> PlaylistPlaybackIntent.Play
        }

        FloatingActionButton(
            onClick = onToggleLooping,
            containerColor = loopingContainerColor,
            contentColor = loopingContentColor,
        ) {
            Icon(
                painter = painterResource(loopingIcon),
                contentDescription = stringResource(PlaylistR.string.playlist_desc_toggle_looping)
            )
        }
        FloatingActionButton(
            onClick = {
                if (playbackState.playlist.voices.isNotEmpty()) {
                    onPlayOrStop(playbackAction)
                }
            },
        ) {
            Icon(
                painter = painterResource(playIcon),
                contentDescription = stringResource(PlaylistR.string.playlist_desc_playback_control)
            )
        }
    }
}

@Preview
@Composable
private fun PlaylistFloatingActionButtonPreview() {
    ShigureUiButtonAppComposeImplementationTheme {
        Card {
            PlaylistFloatingActionButton(
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun PlaylistUpsertDialog(
    modifier: Modifier = Modifier,
    state: PlaylistUpsertState.Draft,
    onStateUpdate: (PlaylistUpsertState) -> Unit = {},
    onSubmit: ()->Unit = {},
    onCancel: ()->Unit = {},
) = Dialog(onDismissRequest = onCancel) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val title = when (state.action) {
                PlaylistUpsertIntent.Insert -> PlaylistR.string.playlist_upsert_dialog_insert_title
                is PlaylistUpsertIntent.Update -> PlaylistR.string.playlist_upsert_dialog_update_title
            }

            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge,
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                OutlinedTextField(
                    label = {
                        Text(stringResource(PlaylistR.string.playlist_upsert_name_label))
                    },
                    value = state.name,
                    onValueChange = { onStateUpdate(state.copy(name = it)) },
                    placeholder = { Text(stringResource(PlaylistR.string.playlist_upsert_name_placeholder)) },
                    modifier = Modifier.fillMaxWidth()
                )
                if (PlaylistUpsertError.BlankName in state.errors) {
                    ErrorMessageStrip(
                        message = stringResource(PlaylistR.string.playlist_upsert_error_blank_name),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (PlaylistUpsertError.ReplicatedName in state.errors) {
                    ErrorMessageStrip(
                        message = stringResource(PlaylistR.string.playlist_upsert_error_replicated_name),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                if (PlaylistUpsertError.NameNotChanged in state.errors) {
                    InfoMessageStrip(
                        message = stringResource(PlaylistR.string.playlist_upsert_info_name_not_changed),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = onSubmit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    enabled = state.errors.isEmpty()
                ) {
                    Text(stringResource(PlaylistR.string.playlist_upsert_dialog_submit))
                }
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(stringResource(PlaylistR.string.playlist_upsert_dialog_cancel))
                }
            }
        }
    }
}

@Preview(locale = "ja")
@Composable
private fun PlaylistUpsertDialogPreview(modifier: Modifier = Modifier) {
    ShigureUiButtonAppComposeImplementationTheme {
        PlaylistUpsertDialog(
            state = PlaylistUpsertState.Draft(
                action = PlaylistUpsertIntent.Insert,
                name = "新播放列表",
                errors = listOf(PlaylistUpsertError.ReplicatedName)
            ),
            modifier = modifier
        )
    }
}

@Composable
private fun PlaylistDeleteDialog(
    modifier: Modifier = Modifier,
    onSubmit: ()->Unit = {},
    onCancel: ()->Unit = {},
) = Dialog(onDismissRequest = onCancel) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = stringResource(PlaylistR.string.playlist_delete_dialog_title),
                style = MaterialTheme.typography.titleLarge,
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = stringResource(PlaylistR.string.playlist_delete_dialog_message),
                style = MaterialTheme.typography.bodyLarge
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.End)
            ) {
                Button(
                    onClick = onSubmit,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                ) {
                    Text(stringResource(PlaylistR.string.playlist_delete_dialog_proceed))
                }
                Button(
                    onClick = onCancel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Text(stringResource(PlaylistR.string.playlist_delete_dialog_cancel))
                }
            }
        }
    }
}