package prac.tanken.shigure.ui.subaci.ui.category

import dagger.hilt.android.lifecycle.HiltViewModel
import prac.tanken.shigure.ui.subaci.data.model.Category
import prac.tanken.shigure.ui.subaci.data.model.CategoryDTO
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    resRepository: ResRepository
) : LoadingViewModel() {
    lateinit var categories: List<Category>
    lateinit var voices: List<Voice>
    val categoriesUi: List<CategoryDTO>
        get() = categories.map { it.toCategoryDTO() }

    init {
        loading {
            voices = resRepository.loadVoices()
            categories = resRepository.loadCategories()
        }
    }

    fun selectByIndex(index: Int) = voices.filter { voice ->
        voice.id in categories[index].idList.map { it.id }
    }

    fun selectByDTO(dto: CategoryDTO): List<Voice> {
        val selectedCategory = categories.filter { it.sectionId == dto.sectionId }[0]
        return voices.filter { voice ->
            voice.id in selectedCategory.idList.map { it.id }
        }
    }
}