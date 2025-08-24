package prac.tanken.shigure.ui.subaci.core.ui

import android.content.Context
import android.content.res.AssetManager
import android.graphics.Typeface
import androidx.compose.ui.text.font.AndroidFont
import androidx.compose.ui.text.font.FontLoadingStrategy
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight

class SUBACIAssetsFont(
    val assetManager: AssetManager,
    val path: String,
    override val weight: FontWeight = FontWeight.Normal,
    override val style: FontStyle = FontStyle.Normal,
    val ttcIndex: Int? = null,
    variationSettings: FontVariation.Settings
) : AndroidFont(
    FontLoadingStrategy.Blocking,
    SUBACITypefaceLoader,
    variationSettings
) {
    internal fun doLoad(context: Context): Typeface {
        return Typeface.Builder(assetManager, path)
            .apply { ttcIndex?.let { setTtcIndex(ttcIndex) } }
            .setFontVariationSettings(variationSettings.toVariationSettings(context))
            .build()
    }

    private var didInitWithContext: Boolean = false

    // subclasses MUST initialize this by calling doLoad(null) - after overriding doLoad as final
    internal var typeface: Typeface? = null

    internal fun loadCached(context: Context): Typeface {
        if (!didInitWithContext && typeface == null) {
            typeface = doLoad(context)
        }
        didInitWithContext = true
        return typeface!!
    }
}