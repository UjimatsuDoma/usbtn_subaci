package prac.tanken.shigure.ui.subaci.feature.playlist.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import prac.tanken.shigure.ui.subaci.feature.playlist.PlaylistViewModel
import prac.tanken.shigure.ui.subaci.feature.playlist.ui.PlaylistScreen

fun NavGraphBuilder.playlistScreen() = composable<Playlist> {
    val viewmodel = hiltViewModel<PlaylistViewModel>()

    PlaylistScreen(viewModel = viewmodel)
}