package prac.tanken.shigure.ui.subaci.ui.playlist

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelectionVO
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.util.CallbackInvokedAsIs
import prac.tanken.shigure.ui.subaci.ui.NotoSansJP
import prac.tanken.shigure.ui.subaci.ui.NotoSerifJP
import prac.tanken.shigure.ui.subaci.ui.NotoSerifMultiLang
import prac.tanken.shigure.ui.subaci.ui.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.ui.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistPlaybackIntent
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistPlaybackState
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertError
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertIntent
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistUpsertState
import com.microsoft.fluent.mobile.icons.R as FluentR
import prac.tanken.shigure.ui.subaci.R as TankenR

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Column(modifier.fillMaxWidth()) {
        if (isLoading) {
            LoadingTopBar()
            LoadingScreenBody(Modifier.weight(1f))
        } else {
            val playlistsSelections by viewModel.playlistsSelections
            val selectedPlaylist by viewModel.selectedPlaylist
            val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()
            val looping by viewModel.isLooping.collectAsStateWithLifecycle()
            val upsertState by viewModel.upsertState.collectAsStateWithLifecycle()

            if (playlistsSelections.isEmpty()) {
                NoPlaylistsTopBar(
                    onAddPlaylist = viewModel::showInsertDialog
                )
                NoPlaylistsScreen(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )
            } else {
                PlaylistTopBar(
                    playlistSelection = playlistsSelections,
                    selected = selectedPlaylist?.toSelectionVO(),
                    onPlaylistSelect = viewModel::selectPlaylist,
                    onAddPlaylist = viewModel::showInsertDialog,
                    onDeletePlaylist = viewModel::deletePlaylist,
                    playbackState = playbackState,
                    onPlayOrStop = viewModel::dispatchPlaybackIntent,
                    looping = looping,
                    onToggleLooping = viewModel::toggleLooping,
                    onShowUpdateDialog = viewModel::showUpdateDialog
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedPlaylist?.playlistItems?.isEmpty() == true) {
                        PlaylistNoItemScreen(Modifier.fillMaxSize())
                    } else {
                        PlaylistScreen(
                            playbackState = playbackState,
                            voices = selectedPlaylist?.playlistItems,
                            onItemClicked = viewModel::playItem,
                            onItemMove = viewModel::movePlaylistItem,
                            onItemDelete = viewModel::removePlaylistItem,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            if (upsertState is PlaylistUpsertState.Draft) {
                val state = upsertState as PlaylistUpsertState.Draft

                PlaylistUpsertDialog(
                    state = state,
                    onStateUpdate = viewModel::updateUpsertState,
                    onSubmit = viewModel::submitUpsert,
                    onCancel = viewModel::cancelUpsert
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoPlaylistsTopBar(
    onAddPlaylist: CallbackInvokedAsIs,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(TankenR.string.playlist_no_playlists),
                fontWeight = FontWeight.Bold,
                fontFamily = NotoSerifMultiLang
            )
        },
        actions = {
            IconButton(onClick = onAddPlaylist) {
                Icon(
                    painter = painterResource(FluentR.drawable.ic_fluent_add_24_regular),
                    contentDescription = stringResource(TankenR.string.playlist_desc_add_playlist)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
internal fun NoPlaylistsScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = {
            Text(
                stringResource(TankenR.string.playlist_no_playlists_content),
                Modifier.fillMaxWidth(0.7f),
                textAlign = TextAlign.Center,
            )
        }
    )
}

@Composable
internal fun PlaylistNoItemScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = {
            Text(
                stringResource(TankenR.string.playlist_no_item),
                Modifier.fillMaxWidth(0.7f),
                textAlign = TextAlign.Center,
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaylistTopBar(
    playlistSelection: List<PlaylistSelectionVO>,
    playbackState: PlaylistPlaybackState,
    selected: PlaylistSelectionVO?,
    looping: Boolean,
    onToggleLooping: () -> Unit,
    onPlaylistSelect: (Int) -> Unit,
    onAddPlaylist: () -> Unit,
    onDeletePlaylist: suspend () -> Unit,
    onPlayOrStop: (PlaylistPlaybackIntent) -> Unit,
    onShowUpdateDialog: (PlaylistSelectionVO) -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    var expanded by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        navigationIcon = {
            val playIcon = when (playbackState) {
                is PlaylistPlaybackState.Playing -> FluentR.drawable.ic_fluent_stop_24_filled
                PlaylistPlaybackState.Stopped -> FluentR.drawable.ic_fluent_play_24_filled
            }
            val loopingIcon = if (looping) {
                FluentR.drawable.ic_fluent_arrow_clockwise_24_filled
            } else {
                FluentR.drawable.ic_fluent_arrow_clockwise_24_regular
            }
            val playbackAction = when (playbackState) {
                is PlaylistPlaybackState.Playing -> PlaylistPlaybackIntent.Stop
                PlaylistPlaybackState.Stopped -> PlaylistPlaybackIntent.Play
            }

            Row {
                IconButton(onClick = { onPlayOrStop(playbackAction) }) {
                    Icon(
                        painter = painterResource(playIcon),
                        contentDescription = stringResource(TankenR.string.playlist_desc_playback_control)
                    )
                }
                IconButton(onClick = onToggleLooping) {
                    Icon(
                        painter = painterResource(loopingIcon),
                        contentDescription = stringResource(TankenR.string.playlist_desc_toggle_looping)
                    )
                }
            }
        },
        title = {
            Column {
                Row(
                    modifier = Modifier
                        .clickable {
                            expanded = !expanded
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

                    Text(
                        text = selected?.playlistName
                            ?: stringResource(TankenR.string.playlist_select_playlist),
                        fontFamily = NotoSerifJP,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.basicMarquee()
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = trailingIcon,
                        contentDescription = null
                    )
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(200.dp)
                ) {
                    playlistSelection.forEach { selection ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = selection.playlistName,
                                    fontFamily = NotoSansJP,
                                )
                            },
                            onClick = {
                                onPlaylistSelect(selection.id)
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
        actions = {
            if (playbackState !is PlaylistPlaybackState.Playing) {
                var expanded by rememberSaveable { mutableStateOf(false) }

                IconButton(
                    onClick = { scope.launch { onAddPlaylist() } }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_add_24_regular),
                        contentDescription = stringResource(TankenR.string.playlist_desc_add_playlist)
                    )
                }
                IconButton(
                    onClick = { expanded = !expanded }
                ) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_more_vertical_24_filled),
                        contentDescription = stringResource(TankenR.string.playlist_desc_overflow_menu)
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(FluentR.drawable.ic_fluent_bin_full_24_filled),
                                contentDescription = stringResource(TankenR.string.playlist_desc_delete_playlist)
                            )
                        },
                        text = { Text(stringResource(TankenR.string.playlist_delete_playlist)) },
                        onClick = {
                            scope.launch { onDeletePlaylist() }
                            expanded = false
                        }
                    )
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(
                                painter = painterResource(FluentR.drawable.ic_fluent_rename_24_filled),
                                contentDescription = stringResource(TankenR.string.playlist_desc_rename_playlist)
                            )
                        },
                        text = { Text(stringResource(TankenR.string.playlist_rename_playlist)) },
                        onClick = {
                            selected?.let { onShowUpdateDialog(it) }
                            expanded = false
                        }
                    )
                }

            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun PlaylistScreen(
    playbackState: PlaylistPlaybackState,
    voices: List<Voice>?,
    onItemClicked: (Int) -> Unit,
    onItemMove: (Int, Boolean) -> Unit,
    onItemDelete: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    voices?.let {
        LazyColumn(
            modifier = modifier.fillMaxSize(),
            state = lazyListState,
            horizontalAlignment = Alignment.Start,
        ) {
            itemsIndexed(
                items = voices,
                key = { index, voice -> Pair(index, voice) }
            ) { index, voice ->

                Column {
                    var expanded by rememberSaveable { mutableStateOf(false) }

                    Text(
                        text = voice.label,
                        fontFamily = NotoSerifJP,
                        fontSize = 24.sp,
                        fontWeight = when (playbackState) {
                            is PlaylistPlaybackState.Playing -> {
                                if (playbackState.index == index) FontWeight.Bold
                                else FontWeight.Normal
                            }

                            PlaylistPlaybackState.Stopped -> FontWeight.Normal
                        },
                        modifier = Modifier
                            .combinedClickable(
                                onClick = { onItemClicked(index) },
                                onLongClick = {
                                    if (playbackState !is PlaylistPlaybackState.Playing)
                                        expanded = true
                                }
                            )
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(TankenR.string.playlist_move_up)) },
                            enabled = index in 1..voices.lastIndex,
                            onClick = {
                                expanded = false
                                onItemMove(index, true)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(TankenR.string.playlist_move_down)) },
                            enabled = index in 0 until voices.lastIndex,
                            onClick = {
                                expanded = false
                                onItemMove(index, false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text(stringResource(TankenR.string.playlist_delete_item)) },
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
    } ?: @Composable {
        Text(
            text = stringResource(TankenR.string.playlist_select_playlist_content),
        )
    }

}

@Composable
internal fun PlaylistUpsertDialog(
    state: PlaylistUpsertState.Draft,
    onStateUpdate: suspend (PlaylistUpsertState) -> Unit,
    onSubmit: CallbackInvokedAsIs,
    onCancel: CallbackInvokedAsIs,
    modifier: Modifier = Modifier,
) {
    val scope = rememberCoroutineScope()

    Dialog(onDismissRequest = onSubmit) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp),
            ) {
                val title = when (state.action) {
                    PlaylistUpsertIntent.Insert -> TankenR.string.playlist_upsert_dialog_insert_title
                    is PlaylistUpsertIntent.Update -> TankenR.string.playlist_upsert_dialog_update_title
                }

                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleLarge,
                    fontSize = 32.sp
                )
                Spacer(Modifier.height(16.dp))
                TextField(
                    value = state.name,
                    onValueChange = { scope.launch { onStateUpdate(state.copy(name = it)) } }
                )
                if (PlaylistUpsertError.BlankName in state.errors) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.error,
                            contentColor = MaterialTheme.colorScheme.onError
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null
                        )
                        Text(text = stringResource(TankenR.string.playlist_upsert_error_blank_name))
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
                        )
                    ) {
                        Text(stringResource(TankenR.string.playlist_upsert_dialog_submit))
                    }
                    Button(
                        onClick = onCancel,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        )
                    ) {
                        Text(stringResource(TankenR.string.playlist_upsert_dialog_cancel))
                    }
                }
            }
        }
    }
}