package prac.tanken.shigure.ui.subaci.feature.playlist.model

sealed interface PlaylistUpsertState {
    data object Closed : PlaylistUpsertState

    data class Draft(
        // 动作：添加播放列表；重命名
        val action: PlaylistUpsertIntent,
        // 播放列表名称：运行期间暂存，提交后清空
        val name: String,
        // 错误列表：每次播放列表名称改变时刷新
        val errors: List<PlaylistUpsertError> = emptyList(),
    ) : PlaylistUpsertState
}