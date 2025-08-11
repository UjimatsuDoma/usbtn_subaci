package prac.tanken.shigure.ui.subaci.domain

import kotlinx.coroutines.flow.combineTransform
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.core.data.model.playlist.playlistNotSelected
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.data.util.parseJsonString
import prac.tanken.shigure.ui.subaci.ui.playlist.model.PlaylistVO
import prac.tanken.shigure.ui.subaci.ui.playlist.model.playlistNotSelectedVO
import prac.tanken.shigure.ui.subaci.ui.playlist.model.toPlaylistVoiceVO
import prac.tanken.shigure.ui.subaci.R as TankenR

class PlaylistUseCase(
    val playlistRepository: PlaylistRepository,
    val resRepository: ResRepository,
) : BaseUseCase() {
    val playlistsFlow = playlistRepository.playlistsFlow
    val playlistSelectedFlow = playlistRepository.playlistSelectedFlow

    val selectedPlaylist = combineTransform(
        playlistSelectedFlow, playlistsFlow, resRepository.voicesFlow
    ) { playlistSelected, playlists, voices ->
        emit(UseCaseEvent.Loading)
        val event = suspendTryOrFail {
            if (playlistSelected == playlistNotSelected)
                UseCaseEvent.Success(playlistNotSelectedVO)
            else {
                val entity = playlists.first { it.id == playlistSelected.selectedId }
                val voicesIdList = Json.decodeFromString<List<String>>(entity.playlistItems)
                val voices = voicesIdList
                    .map { id -> voices.first { it.id == id }.toPlaylistVoiceVO() }
                val playlistVO = PlaylistVO(
                    entity.id,
                    entity.playlistName,
                    voices
                )
                UseCaseEvent.Success(playlistVO)
            }
        }
        emit(event)
    }

    // 操作播放列表整体

    suspend fun createPlaylist(name: String) {
        val createdId = playlistRepository.createPlaylist(name)
        selectPlaylist(createdId)
    }

    suspend fun deletePlaylist(id: Long) = suspendTryOrFail {
        unselectPlaylist()
        val entity = playlistRepository.getById(id)
        playlistRepository.deletePlaylist(entity)
        // 如果已无播放列表，则保持未选状态，否则选择ID最大的那个。
        playlistRepository.getMaxId()?.let { selectPlaylist(it) }
        return@suspendTryOrFail UseCaseEvent.Success(Unit)
    }

    suspend fun renamePlaylist(id: Long, name: String) = suspendTryOrFail {
        val entity = playlistRepository.getById(id)
        playlistRepository.updatePlaylist(entity.copy(playlistName = name))
        return@suspendTryOrFail UseCaseEvent.Success(Unit)
    }

    suspend fun selectPlaylistById(id: Long) = playlistRepository.getById(id)

    suspend fun selectPlaylistByName(name: String) =
        playlistRepository.getByName(name).run {
            if (isNotEmpty()) first()
            else null
        }

    // 操作播放列表内部项目

    suspend fun addToPlaylist(plistId: Long, voiceId: String) = suspendTryOrFail {
        val entity = playlistRepository.getById(plistId)
        val oldList = Json.decodeFromString<List<String>>(entity.playlistItems)
        if (oldList.contains(voiceId)) {
            throw IllegalStateException(
                resRepository.stringRes(TankenR.string.error_playlist_duplicate_item)
            )
        }
        val newList = oldList + voiceId
        playlistRepository.updatePlaylist(
            entity.copy(playlistItems = Json.encodeToString(newList))
        )
        return@suspendTryOrFail UseCaseEvent.Success(Unit)
    }

    suspend fun removePlaylistItem(plistId: Long, index: Int) = suspendTryOrFail {
        val entity = playlistRepository.getById(plistId)

        val items = parseJsonString<List<String>>(entity.playlistItems)
        if (index !in items.indices) {
            val message = resRepository.stringRes(TankenR.string.error_illegal_index)
            throw IllegalStateException(message)
        }

        val newArr = items.toMutableList().apply { removeAt(index) }.toList()
        val newArrStr = Json.encodeToString(newArr)
        playlistRepository.updatePlaylist(entity.copy(playlistItems = newArrStr))
        return@suspendTryOrFail UseCaseEvent.Success(Unit)
    }

    suspend fun movePlaylistItem(plistId: Long, index: Int, moveUp: Boolean) = suspendTryOrFail {
        val entity = playlistRepository.getById(plistId)

        // 检查下标越界
        val items = parseJsonString<List<String>>(entity.playlistItems)
        if (index !in items.indices) {
            val message = resRepository.stringRes(TankenR.string.error_illegal_index)
            throw IllegalStateException(message)
        }
        val moveUpValid = moveUp && index in 1..items.lastIndex
        val moveDownValid = !moveUp && index in 0 until items.lastIndex
        if (!(moveUpValid || moveDownValid)) {
            val message = resRepository.stringRes(TankenR.string.error_playlist_move_item_oob)
            throw IllegalStateException(message)
        }

        val targetIndex = if (moveUp) index - 1 else index + 1
        val newArr = items.toMutableList().also { arr ->
            arr[index] = arr[targetIndex].apply {
                arr[targetIndex] = arr[index]
            }
        }.toList()
        val newArrStr = Json.encodeToString(newArr)
        playlistRepository.updatePlaylist(entity.copy(playlistItems = newArrStr))
        return@suspendTryOrFail UseCaseEvent.Success(Unit)
    }

    // 操作播放列表选择项
    suspend fun selectPlaylist(id: Long) = playlistRepository.selectPlaylist(id)

    suspend fun unselectPlaylist() = playlistRepository.unselectPlaylist()
}