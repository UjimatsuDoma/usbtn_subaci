@file:OptIn(ExperimentalMaterial3Api::class)

package prac.tanken.shigure.ui.subaci.feature.playlist.ui

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.core.data.model.Playlist
import com.microsoft.fluent.mobile.icons.R as FluentR
import prac.tanken.shigure.ui.subaci.feature.playlist.R as PlaylistR

@Composable
internal fun PlaylistTopBar(
    modifier: Modifier = Modifier,
    playlists: List<Playlist> = emptyList(),
    selectedPlaylist: Playlist? = null,
    onAddPlaylist: () -> Unit = {},
    onDeletePlaylist: () -> Unit = {},
    onPlaylistSelect: (Long) -> Unit = {},
    onShowUpdateDialog: () -> Unit = {},
) {
    val hasPlaylist = playlists.isNotEmpty()
    val title = selectedPlaylist?.playlistName
        ?: if (!hasPlaylist) {
            stringResource(PlaylistR.string.playlist_no_playlists)
        } else {
            stringResource(PlaylistR.string.playlist_select_playlist)
        }

    var expanded by rememberSaveable { mutableStateOf(false) }

    TopAppBar(
        title = {
            Column {
                Row(
                    modifier = Modifier
                        .clickable {
                            if (hasPlaylist) {
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

                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Black,
                        modifier = Modifier.basicMarquee()
                    )
                    if (hasPlaylist) {
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            painter = trailingIcon,
                            contentDescription = null
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(200.dp)
                ) {
                    if (hasPlaylist) {
                        playlists.forEach { selection ->
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
                onClick = { onShowUpdateDialog() }
            ) {
                Icon(
                    painter = painterResource(FluentR.drawable.ic_fluent_rename_24_filled),
                    contentDescription = stringResource(PlaylistR.string.playlist_desc_rename_playlist)
                )
            }
        },
        modifier = modifier
    )
}