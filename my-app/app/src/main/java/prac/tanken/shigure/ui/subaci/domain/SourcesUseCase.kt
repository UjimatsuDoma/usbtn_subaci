package prac.tanken.shigure.ui.subaci.domain

import kotlinx.coroutines.flow.combineTransform
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.ui.sources.model.SourcesListItem

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