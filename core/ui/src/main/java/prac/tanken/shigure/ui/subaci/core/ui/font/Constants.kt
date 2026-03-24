package prac.tanken.shigure.ui.subaci.core.ui.font

import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.io.File

val fontsDir
    @Composable get() = File(LocalContext.current.applicationContext.filesDir, "fonts")

val LocalJPFont = compositionLocalOf { JPFonts.default }