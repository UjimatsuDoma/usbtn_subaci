package prac.tanken.shigure.ui.subaci.ui.playlist

import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import prac.tanken.shigure.ui.subaci.data.model.PlaylistSelectionVO
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.ui.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.ui.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.ui.theme.NotoSansJP
import prac.tanken.shigure.ui.subaci.ui.theme.NotoSerifJP
import com.microsoft.fluent.mobile.icons.R as FluentR

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
            val playlistsSelections = viewModel.playlistsSelections
            val selectedPlaylistVO by viewModel.selectedPlaylistVO
            val selectedPlaylist by viewModel.selectedPlaylist
            val playbackState by viewModel.playbackState.collectAsStateWithLifecycle()

            if (playlistsSelections.isEmpty()) {
                NoPlaylistsTopBar(
                    onAddPlaylist = viewModel::createPlaylist
                )
                Box(Modifier.weight(1f))
            } else {
                PlaylistTopBar(
                    playlistSelection = playlistsSelections,
                    selected = selectedPlaylistVO,
                    onPlaylistSelect = viewModel::selectPlaylist,
                    onAddPlaylist = viewModel::createPlaylist,
                    onDeletePlaylist = viewModel::deletePlaylist,
                    playbackState = playbackState,
                    onPlayOrStop = viewModel::dispatchPlaybackIntent
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedPlaylist?.playlistItems?.isEmpty() == true) {
                        PlaylistNoItemScreen()
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
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NoPlaylistsTopBar(
    onAddPlaylist: suspend () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()

    CenterAlignedTopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = "No playlists",
                fontWeight = FontWeight.Bold,
                fontFamily = NotoSerifJP
            )
        },
        actions = {
            IconButton(
                onClick = { scope.launch { onAddPlaylist() } }
            ) {
                Icon(
                    painter = painterResource(FluentR.drawable.ic_fluent_add_24_regular),
                    contentDescription = null
                )
            }
        },
        modifier = modifier
    )
}

@Composable
internal fun PlaylistNoItemScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text("No items in this playlist.")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun PlaylistTopBar(
    playlistSelection: List<PlaylistSelectionVO>,
    playbackState: PlaylistPlaybackState,
    selected: PlaylistSelectionVO?,
    onPlaylistSelect: (Int) -> Unit,
    onAddPlaylist: suspend () -> Unit,
    onDeletePlaylist: suspend () -> Unit,
    onPlayOrStop: (PlaylistPlaybackIntent) -> Unit,
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
            val action = when (playbackState) {
                is PlaylistPlaybackState.Playing -> PlaylistPlaybackIntent.Stop
                PlaylistPlaybackState.Stopped -> PlaylistPlaybackIntent.Play
            }

            Row {
                IconButton(onClick = { onPlayOrStop(action) }) {
                    Icon(
                        painter = painterResource(playIcon),
                        contentDescription = null
                    )
                }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(FluentR.drawable.ic_fluent_arrow_clockwise_24_filled),
                        contentDescription = null
                    )
                }
            }
        },
        title = {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
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
                        text = selected?.playlistName ?: "__SELECT__",
                        fontFamily = NotoSerifJP,
                        fontWeight = FontWeight.Bold
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
                                    fontFamily = NotoSansJP
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
            var expanded by rememberSaveable { mutableStateOf(false) }

            IconButton(
                onClick = { scope.launch { onAddPlaylist() } }
            ) {
                Icon(
                    painter = painterResource(FluentR.drawable.ic_fluent_add_24_regular),
                    contentDescription = null
                )
            }
            IconButton(
                onClick = { expanded = !expanded }
            ) {
                Icon(
                    painter = painterResource(FluentR.drawable.ic_fluent_more_vertical_24_filled),
                    contentDescription = "Overflow menu"
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
                            contentDescription = null
                        )
                    },
                    text = { Text("Delete playlist") },
                    onClick = { scope.launch { onDeletePlaylist() } }
                )
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
//                        text = "$index. ${voice.label}",
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
                                onLongClick = { expanded = true }
                            )
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Move up") },
                            enabled = index in 1..voices.lastIndex,
                            onClick = {
                                expanded = false
                                onItemMove(index, true)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Move down") },
                            enabled = index in 0 until voices.lastIndex,
                            onClick = {
                                expanded = false
                                onItemMove(index, false)
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Delete") },
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
        Text("Please select playlist.")
    }

}