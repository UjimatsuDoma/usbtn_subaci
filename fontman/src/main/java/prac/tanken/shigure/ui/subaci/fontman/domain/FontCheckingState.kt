package prac.tanken.shigure.ui.subaci.fontman.domain

import kotlinx.serialization.Serializable

@Serializable
sealed interface FontCheckingState {
    @Serializable
    data object Checking : FontCheckingState
    @Serializable
    data object Passed : FontCheckingState
    @Serializable
    data object NeedDecompress : FontCheckingState
    @Serializable
    data object NeedInstantiate : FontCheckingState
    @Serializable
    data object Cleaning : FontCheckingState
}