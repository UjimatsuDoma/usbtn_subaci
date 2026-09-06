package prac.tanken.shigure.ui.subaci.feature.playlist

import androidx.annotation.IntRange
import prac.tanken.shigure.ui.subaci.core.data.model.Playlist
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiEffect
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiIntent
import prac.tanken.shigure.ui.subaci.feature.base.mvi.UiState

object PlaylistContract {
    data class State(
        // 一级加载状态：是否加载出所有的播放列表
        val playlistsUiState: PlaylistsUiState = PlaylistsUiState.Loading,
        // 二级加载状态：是否加载出当前选择的播放列表
        val playlistUiState: PlaylistUiState = PlaylistUiState.StandBy,
    ) : UiState

    sealed interface PlaylistsUiState {
        data object Loading : PlaylistsUiState
        data class Loaded(
            val playlists: List<Playlist> = emptyList(),
        ) : PlaylistsUiState

        data class Error(val message: String) : PlaylistsUiState {
            companion object {
                fun fromThrowable(throwable: Throwable) =
                    Error(throwable.message ?: throwable.javaClass.simpleName)
            }
        }
    }

    sealed interface PlaylistUiState {
        data object StandBy : PlaylistUiState
        data object Loading : PlaylistUiState
        data class Loaded(
            val selectedPlaylistIndex: Long,
            val voices: List<Voice>,
        ) : PlaylistUiState {
            init {
                require(selectedPlaylistIndex >= 0L) {
                    "selectedPlaylistIndex cannot be negative."
                }
            }
        }

        data class Error(val message: String) : PlaylistUiState {
            companion object {
                fun fromThrowable(throwable: Throwable) =
                    Error(throwable.message ?: throwable.javaClass.simpleName)
            }
        }
    }

    sealed interface Intent : UiIntent {
        data class SelectPlaylist(val playlist: Playlist) : Intent
    }

    sealed interface Effect : UiEffect
}