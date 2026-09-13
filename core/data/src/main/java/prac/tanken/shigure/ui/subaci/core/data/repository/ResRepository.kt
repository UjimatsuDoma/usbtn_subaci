package prac.tanken.shigure.ui.subaci.core.data.repository

import android.content.Context
import android.content.res.AssetManager
import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import prac.tanken.shigure.ui.subaci.core.common.io.readText
import prac.tanken.shigure.ui.subaci.core.common.serialization.parseJsonString
import prac.tanken.shigure.ui.subaci.core.data.model.Voice
import prac.tanken.shigure.ui.subaci.core.data.model.Source
import prac.tanken.shigure.ui.subaci.core.data.model.Category
import prac.tanken.shigure.ui.subaci.core.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.core.ui.font.NotoStyle
import prac.tanken.shigure.ui.subaci.core.ui.getNotoFamilyByLocalesNonComposable
import java.io.File
import javax.inject.Inject

class ResRepository @Inject constructor(
    val res: Resources,
    val am: AssetManager,
    @ApplicationContext val appContext: Context,
) {
    fun stringRes(@StringRes stringRes: Int) = res.getString(stringRes)

    fun getVoiceAFD(vr: VoiceReference) = am.openFd("subaciAudio/${vr.id}.mp3")

    fun getVariableFontFamily(notoStyle: NotoStyle) =
        getNotoFamilyByLocalesNonComposable(appContext, notoStyle)
}