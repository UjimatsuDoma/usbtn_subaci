package prac.tanken.shigure.ui.subaci.feature.base.mvi

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
open class UiError(
    @SerialName("UiError_message")
    open val message: String?,
    @SerialName("UiError_stackTrace")
    open val stackTrace: String
) {
    companion object {
        fun fromThrowable(throwable: Throwable) =
            UiError(
                message = throwable.message,
                stackTrace = throwable.stackTraceToString()
            )
    }
}