package prac.tanken.shigure.ui.subaci.fontman

import android.content.Context
import android.util.Log
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Test
import prac.tanken.shigure.ui.subaci.core.data.repository.InternalStorageRepository
import prac.tanken.shigure.ui.subaci.core.data.repository.ResRepository
import prac.tanken.shigure.ui.subaci.fontman.domain.FontDecompressUseCase

class FontDecompressTest {
    @Test
    fun decompress_noto_font_from_assets() {
        val cxt = ApplicationProvider.getApplicationContext<Context>()
        val uc = FontDecompressUseCase(
            InternalStorageRepository(cxt),
            ResRepository(cxt.resources, cxt.assets)
        )
        val progressFlow= MutableStateFlow(0f)
        runBlocking {
            launch {
                progressFlow.collect {
                    Log.d("FontDTest", "Progress: $it")
                }
            }
            uc.decompress { progress ->
                progressFlow.value = progress
            }
        }
    }
}