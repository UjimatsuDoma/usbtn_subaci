package prac.tanken.shigure.ui.subaci.base.domain

/**
 * 领域层向视图层返回的结果。
 * 如果是成功则包含返回数据，如果失败（发生异常等）则返回发生的错误。
 *
 * @author UjimatsuDoma
 */
sealed interface UseCaseEvent {
    data object Loading: UseCaseEvent
    data class Success<T>(val data: T) : UseCaseEvent
    data class Info(val message: String) : UseCaseEvent
    data class Error(val message: String) : UseCaseEvent
}