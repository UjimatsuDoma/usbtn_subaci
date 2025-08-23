package prac.tanken.shigure.ui.subaci.sources.domain

import kotlinx.coroutines.flow.combineTransform
import prac.tanken.shigure.ui.subaci.base.domain.BaseUseCase
import prac.tanken.shigure.ui.subaci.base.domain.UseCaseEvent
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.sources.model.SourcesListItem

class SourcesUseCase(
    val resRepository: ResRepository,
) : BaseUseCase() {
    val voicesFlow = resRepository.voicesFlow
    val sourcesFlow = resRepository.sourcesFlow

    val sourcesEventFlow = combineTransform(
        voicesFlow, sourcesFlow
    ) { voices, sources ->
        emit(UseCaseEvent.Loading)
        val event = suspendTryOrFail {
            val newList = sources.map { sourceEntity ->
                val voicesFiltered = voices.filter { it.videoId == sourceEntity.videoId }.toList()
                SourcesListItem(sourceEntity.videoId, sourceEntity.title, voicesFiltered)
            }.toList()
            UseCaseEvent.Success(newList)
        }
        emit(event)
    }
}