package prac.tanken.shigure.ui.subaci.core.domain.model

import prac.tanken.shigure.ui.subaci.core.ui.UiText

sealed interface UseCaseEvent<out R> {
    data class Success<out R>(val data: R) : UseCaseEvent<R>
}