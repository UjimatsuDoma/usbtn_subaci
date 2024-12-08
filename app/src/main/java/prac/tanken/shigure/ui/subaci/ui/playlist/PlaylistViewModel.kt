package prac.tanken.shigure.ui.subaci.ui.playlist

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import prac.tanken.shigure.ui.subaci.data.database.PlaylistDatabase
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import javax.inject.Inject

@HiltViewModel
class PlaylistViewModel @Inject constructor(
    playlistDatabase: PlaylistDatabase,
    myPlayer: MyPlayer
): ViewModel() {

}