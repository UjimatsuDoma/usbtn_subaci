package prac.tanken.shigure.ui.subaci.feature.playlist.ui

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import prac.tanken.shigure.ui.subaci.core.ui.font.LocalJPFont
import prac.tanken.shigure.ui.subaci.core.ui.util.combineKey
import prac.tanken.shigure.ui.subaci.feature.playlist.PlaylistContract
import prac.tanken.shigure.ui.subaci.feature.playlist.R as PlaylistR

@Composable
internal fun PlaylistVoicesList(
    modifier: Modifier = Modifier,
    playlistUiState: PlaylistContract.PlaylistUiState,
    onItemClicked: (Int) -> Unit,
    onItemMove: (Int, Boolean) -> Unit,
    onItemDelete: (Int) -> Unit,
) {
    val lazyListState = rememberLazyListState()

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        when(playlistUiState) {
            PlaylistContract.PlaylistUiState.StandBy -> {
                Text(stringResource(PlaylistR.string.playlist_select_playlist))
            }
            PlaylistContract.PlaylistUiState.Loading -> {
                CircularProgressIndicator()
            }
            is PlaylistContract.PlaylistUiState.Loaded -> {
                if (playlistUiState.voices.isNotEmpty()) {
                    val voices = playlistUiState.voices

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

                                Text(
                                    text = voice.label,
                                    fontSize = 24.sp,
                                    modifier = Modifier
                                        .combinedClickable(
                                            onClick = { onItemClicked(index) },
                                            onLongClick = {
                                                expanded = true
                                            }
                                        )
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    fontFamily = FontFamily(Font(LocalJPFont.current.fontResId))
                                )

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
            }

            is PlaylistContract.PlaylistUiState.Error -> {
                Text(playlistUiState.message)
            }
        }
    }
}