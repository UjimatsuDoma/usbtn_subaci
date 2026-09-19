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
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.core.domain.R
import prac.tanken.shigure.ui.subaci.core.domain.model.UseCaseEvent
import javax.inject.Inject

typealias AddToPlaylistUseCaseEvent = UseCaseEvent<String, Throwable>

class AddToPlaylistUseCase @Inject constructor(
    val playlistRepository: PlaylistRepository,
    val resRepository: ResRepository,
) {
    operator fun invoke(voice: Voice): Flow<AddToPlaylistUseCaseEvent> {
        return playlistRepository.selectedPlaylistFlow
            .transform { repositoryEvent ->
                when (repositoryEvent) {
                    RepositoryEvent.Working -> Unit

                    is RepositoryEvent.Error -> {
                        emit(UseCaseEvent.Error(repositoryEvent.error))
                    }

                    is RepositoryEvent.Success -> {
                        repositoryEvent.data?.let {
                            try {
                                if(voice.id in it.playlistItemIds) {
                                    emit(
                                        UseCaseEvent.Info(
                                            resRepository.stringRes(
                                                R.string.added_to_playlist_usecase_info_already_added
                                            )
                                        )
                                    )
                                } else {
                                    playlistRepository.updatePlaylist(
                                        it.copy(
                                            playlistItemIds = it.playlistItemIds
                                                .toMutableList()
                                                .apply { add(voice.id) }
                                                .toList()
                                        ).toEntity()
                                    )

                                    val updated = playlistRepository.getById(it.id)
                                        .convertToPlaylist()
                                        .playlistItemIds.any { voice.id == it }

                                    check(updated) {
                                        resRepository.stringRes(
                                            R.string.add_to_playlist_error_not_added
                                        )
                                    }

                                    emit(
                                        UseCaseEvent.Success(
                                            resRepository.stringRes(
                                                R.string.add_to_playlist_usecase_success
                                            )
                                        )
                                    )
                                }
                            } catch (e: Exception) {
                                emit(UseCaseEvent.Error(e))
                            }
                        } ?: emit(
                            UseCaseEvent.Error(
                                IllegalStateException(
                                    resRepository.stringRes(
                                        R.string.add_to_playlist_usecase_error_no_selected
                                    )
                                )
                            )
                        )
                    }
                }
            }
    }
}