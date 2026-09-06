package prac.tanken.shigure.ui.subaci.core.domain.usecase.playlist

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import prac.tanken.shigure.ui.subaci.core.data.model.Playlist
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.playlistNotSelected
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.RepositoryEvent
import prac.tanken.shigure.ui.subaci.core.domain.model.UseCaseEvent
import javax.inject.Inject

typealias AddToPlaylistUseCaseEvent = UseCaseEvent<Boolean, Throwable>

class AddToPlaylistUseCase @Inject constructor(
    val playlistRepository: PlaylistRepository,
) {
    operator fun invoke(voice: Voice): Flow<AddToPlaylistUseCaseEvent> {
        return playlistRepository.playlistSelectedFlow
            .transform { repositoryEvent ->
                when(repositoryEvent) {
                    RepositoryEvent.Working -> Unit
                    is RepositoryEvent.Error -> {
                        emit(UseCaseEvent.Error(repositoryEvent.error))
                    }
                    is RepositoryEvent.Success -> {
                        if(repositoryEvent.data == playlistNotSelected) {
                            emit(UseCaseEvent.Error(IllegalStateException(
                                "No playlist selected."
                            )))
                        } else {
                            try {
                                val playlist = playlistRepository.getById(
                                    repositoryEvent.data.selectedId
                                ).convertToPlaylist()
                                playlistRepository.updatePlaylist(
                                    playlist.copy(
                                        playlistItemIds = buildList {
                                            addAll(playlist.playlistItemIds)
                                            add(voice.id)
                                        }
                                    ).toEntity()
                                )
                                val updated = playlistRepository.getById(playlist.id)
                                    .convertToPlaylist()
                                    .playlistItemIds.any { voice.id == it }
                                emit(UseCaseEvent.Success(updated))
                            } catch (e: Exception) {
                                emit(UseCaseEvent.Error(e))
                            }
                        }
                    }
                }
            }
    }
}