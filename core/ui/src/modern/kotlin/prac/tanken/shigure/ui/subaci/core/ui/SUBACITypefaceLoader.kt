package prac.tanken.shigure.ui.subaci.core.ui

import android.content.Context
import android.graphics.Typeface
import androidx.compose.ui.text.font.AndroidFont

object SUBACITypefaceLoader : AndroidFont.TypefaceLoader {
    override suspend fun awaitLoad(
        context: Context,
        font: AndroidFont
    ): Nothing {
        throw UnsupportedOperationException("All preloaded fonts are blocking.")
    }

    override fun loadBlocking(
        context: Context,
        font: AndroidFont
    ): Typeface =
        (font as SUBACIAssetsFont).loadCached(context)

}