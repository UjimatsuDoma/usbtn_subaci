package prac.tanken.shigure.ui.subaci.fontman.domain

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.fontman.core.FontInstantiateRepository
import javax.inject.Inject

class FontInstantiateUseCase @Inject constructor(
    private val repository: FontInstantiateRepository,
) {
    companion object {
        const val TAG = "FontInstantiate"

        var total = 0
        var currentProcessed = 0
    }

    suspend fun instantiate(onProgress: (Float) -> Unit): FontInstantiateState {
        return withContext(Dispatchers.IO) {
            try {
                repository.instantiateAllWeightsWithProgress {
                    onProgress(it)
                }
                FontInstantiateState.Complete
            } catch (e: Exception) {
                Log.e(TAG, e.message!!)
                FontInstantiateState.Failed
            }
        }
    }
}