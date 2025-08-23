package prac.tanken.shigure.ui.subaci.playlist.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import prac.tanken.shigure.ui.subaci.playlist.ui.PlaylistScreen
import prac.tanken.shigure.ui.subaci.playlist.PlaylistViewModel

fun NavGraphBuilder.playlistScreen() = composable<Playlist> {
    val viewmodel = hiltViewModel<PlaylistViewModel>()

    PlaylistScreen(viewModel = viewmodel)
}