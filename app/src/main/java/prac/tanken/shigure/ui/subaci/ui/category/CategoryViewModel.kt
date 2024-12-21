package prac.tanken.shigure.ui.subaci.ui.category

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.data.model.Category
import prac.tanken.shigure.ui.subaci.data.model.CategoryVO
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.data.player.MyPlayer
import prac.tanken.shigure.ui.subaci.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.ui.LoadingViewModel
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    val resRepository: ResRepository,
    val myPlayer: MyPlayer,
) : LoadingViewModel() {
    private var _categories = mutableStateListOf<Category>()
    val categories: List<Category> get() = _categories
    val categoriesUi: List<CategoryVO>
        get() = categories.map { it.toCategoryVO() }
    private var _selectedVoices = mutableStateOf(emptyList<Voice>())
    val selectedVoices get() = _selectedVoices
    private var _selectedIndex = MutableStateFlow(0)
    val selectedIndex = _selectedIndex.asStateFlow()

    init {
        viewModelScope.launch {
            loading(Dispatchers.IO) {
                _categories.addAll(resRepository.loadCategories())
            }
            selectedIndex.collect { index ->
                updateSelectedVoicesByIndex(index)
            }
        }
    }

    private fun updateSelectedVoicesByIndex(index: Int) {
        loading {
            var voices = emptyList<Voice>()
            withContext(Dispatchers.IO) {
                _selectedVoices.value = emptyList<Voice>()
                voices = resRepository.loadVoices()
            }
            withContext(Dispatchers.Default) {
                val selectedCategory = categories[index]
                val selectedVoices = voices.filter {
                    it.id in selectedCategory.idList.map { it.id }
                }.toList()
                _selectedVoices.value = selectedVoices
            }
        }
    }

    fun onButtonClicked(voiceReference: VoiceReference) =
        viewModelScope.launch(Dispatchers.Default) {
            myPlayer.playByReference(voiceReference)
        }

    fun selectByIndex(index: Int) {
        _selectedIndex.value = index
    }
}