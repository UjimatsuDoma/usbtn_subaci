package prac.tanken.shigure.ui.subaci.feature.playlist.domain

import kotlinx.serialization.json.Json
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.repository.PlaylistRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.feature.base.domain.BaseUseCase
import prac.tanken.shigure.ui.subaci.feature.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.core.common.R as CommonR
import prac.tanken.shigure.ui.subaci.feature.playlist.R as PlaylistR

class PlaylistUseCase(
    val playlistRepository: PlaylistRepository,
    val resRepository: ResRepository,
) : BaseUseCase() {

    // 操作播放列表内部项目

    suspend fun removePlaylistItem(plistId: Long, index: Int) = suspendTryOrFail {
        val entity = playlistRepository.getById(plistId)

        val items = parseJsonString<List<String>>(entity.playlistItems)
        if (index !in items.indices) {
            val message = resRepository.stringRes(CommonR.string.error_illegal_index)
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
            val message = resRepository.stringRes(CommonR.string.error_illegal_index)
            throw IllegalStateException(message)
        }
        val moveUpValid = moveUp && index in 1..items.lastIndex
        val moveDownValid = !moveUp && index in 0 until items.lastIndex
        if (!(moveUpValid || moveDownValid)) {
            val message = resRepository.stringRes(PlaylistR.string.error_playlist_move_item_oob)
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
}