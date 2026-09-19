package prac.tanken.shigure.ui.subaci.feature.playlist.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import prac.tanken.shigure.ui.subaci.core.ui.screen.LoadingIndefinitelyScreen
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import prac.tanken.shigure.ui.subaci.feature.base.component.ErrorMessageStrip
import prac.tanken.shigure.ui.subaci.feature.base.component.InfoMessageStrip
import prac.tanken.shigure.ui.subaci.feature.playlist.PlaylistContract
import prac.tanken.shigure.ui.subaci.feature.playlist.PlaylistViewModel
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertError
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertIntent
import prac.tanken.shigure.ui.subaci.feature.playlist.model.PlaylistUpsertState
import prac.tanken.shigure.ui.subaci.core.common.R as CommonR
import prac.tanken.shigure.ui.subaci.feature.playlist.R as PlaylistR

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    viewModel: PlaylistViewModel,
) {
    val upsertState by viewModel.upsertState.collectAsStateWithLifecycle()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showDeleteDialog by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }

    val lifecycleOwner = LocalLifecycleOwner.current
    LaunchedEffect(lifecycleOwner) {
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            viewModel.effect.collect {
                when(it) {
                    is PlaylistContract.Effect.ShowSnackbar -> snackbarHostState.showSnackbar(it.message)
                }
            }
        }
    }

    when (state.playlistsUiState) {
        PlaylistContract.PlaylistsUiState.Loading -> {
            LoadingIndefinitelyScreen(modifier.fillMaxSize())
        }

        is PlaylistContract.PlaylistsUiState.Error -> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = modifier.fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = stringResource(CommonR.string.error_generic),
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = (state.playlistsUiState as PlaylistContract.PlaylistsUiState.Error).message,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

        is PlaylistContract.PlaylistsUiState.Loaded -> {
            Scaffold(
                modifier = modifier,
                snackbarHost = {
                    SnackbarHost(snackbarHostState)
                },
                topBar = {
                    val playlistsUiState = state.playlistsUiState as PlaylistContract.PlaylistsUiState.Loaded
                    PlaylistTopBar(
                        playlists = playlistsUiState.playlists,
                        selectedPlaylist = playlistsUiState.playlists.firstOrNull {
                            it.id == (state.playlistUiState as? PlaylistContract.PlaylistUiState.Loaded)?.selectedPlaylistIndex
                        },
                        onAddPlaylist = viewModel::showInsertDialog,
                        onDeletePlaylist = { showDeleteDialog = true },
                        onPlaylistSelect = viewModel::selectPlaylist,
                        onShowUpdateDialog = viewModel::showUpdateDialog
                    )
                }
            ) { innerPadding ->
                PlaylistVoicesList(
                    modifier = Modifier
                        .padding(innerPadding)
                        .consumeWindowInsets(innerPadding),
                    onItemClicked = viewModel::playItem,
                    onItemMove = viewModel::movePlaylistItem,
                    onItemDelete = viewModel::removePlaylistItem,
                    playlistUiState = state.playlistUiState,
                )
            }
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
                viewModel.deletePlaylist()
            },
            onCancel = { showDeleteDialog = false }
        )
    }
}

@Composable
private fun PlaylistUpsertDialog(
    modifier: Modifier = Modifier,
    state: PlaylistUpsertState.Draft,
    onStateUpdate: (PlaylistUpsertState) -> Unit = {},
    onSubmit: () -> Unit = {},
    onCancel: () -> Unit = {},
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
    onSubmit: () -> Unit = {},
    onCancel: () -> Unit = {},
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