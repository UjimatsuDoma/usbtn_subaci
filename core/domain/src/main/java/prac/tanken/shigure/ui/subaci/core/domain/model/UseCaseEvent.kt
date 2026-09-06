package prac.tanken.shigure.ui.subaci.core.domain.model

import prac.tanken.shigure.ui.subaci.core.ui.UiText

sealed interface UseCaseEvent<out Result, out Error> {
    data class Success<out R>(val data: R) : UseCaseEvent<R, Nothing>
    data class Error<out E>(val error: E): UseCaseEvent<Nothing, E>
}