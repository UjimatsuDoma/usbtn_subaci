package prac.tanken.shigure.ui.subaci.base.domain

abstract class BaseUseCase {
    open fun <T> tryOrFail(block: () -> UseCaseEvent.Success<T>) =
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            UseCaseEvent.Error(e.message ?: e.javaClass.simpleName)
        }

    open suspend fun <T> suspendTryOrFail(block: suspend () -> UseCaseEvent.Success<T>) =
        try {
            block()
        } catch (e: Exception) {
            e.printStackTrace()
            UseCaseEvent.Error(e.message ?: e.javaClass.simpleName)
        }
}