package prac.tanken.shigure.ui.subaci.core.data.repository

sealed interface RepositoryEvent<out D, out E> {
    data object Working: RepositoryEvent<Nothing, Nothing>
    data class Success<out D>(val data: D): RepositoryEvent<D, Nothing>
    data class Error<out E>(val error: E): RepositoryEvent<Nothing, E>
}