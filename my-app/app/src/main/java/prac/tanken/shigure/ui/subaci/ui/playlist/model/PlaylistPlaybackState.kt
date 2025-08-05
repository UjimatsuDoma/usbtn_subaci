package prac.tanken.shigure.ui.subaci.ui.playlist.model

import kotlinx.serialization.Serializable

/**
 * 播放列表播放状态
 *
 * 存在如下状态：
 * 未选择 - 没有选中播放列表（或不存在播放列表）
 * 正在加载 - 已选中播放列表，正在加载播放列表的内容
 * 加载错误 - 加载播放列表内容过程中出错
 * 停止 - 已加载完成，且未点击播放按钮，此时可以切换、修改、增加或删除播放列表
 * 正在播放 - 已加载完成，且正在播放，此时不能进行前述操作
 */
@Serializable
sealed interface PlaylistPlaybackState {
    @Serializable
    data object StandBy : PlaylistPlaybackState

    @Serializable
    data object Loading : PlaylistPlaybackState

    @Serializable
    data object Error : PlaylistPlaybackState

    @Serializable
    sealed class Loaded(open val playlist: PlaylistVO) : PlaylistPlaybackState {

        data class Stopped(override val playlist: PlaylistVO) : Loaded(playlist) {
            fun play(index: Int) = Playing(playlist, index)
        }

        data class Playing(
            override val playlist: PlaylistVO,
            val index: Int,
        ) : Loaded(playlist) {
            fun stop() = Stopped(playlist)
        }

        fun playlistHasItems() = playlist.voices.isNotEmpty()
    }
}