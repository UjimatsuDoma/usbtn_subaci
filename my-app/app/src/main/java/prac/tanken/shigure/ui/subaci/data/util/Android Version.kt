package prac.tanken.shigure.ui.subaci.data.util

import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import prac.tanken.shigure.ui.subaci.R

private fun notLessThan(version: Int) = Build.VERSION.SDK_INT >= version

fun versionTranslate(version: Int) = when (version) {
    Build.VERSION_CODES.O,
    Build.VERSION_CODES.O_MR1 -> 8

    Build.VERSION_CODES.P -> 9
    Build.VERSION_CODES.Q -> 10

    Build.VERSION_CODES.S,
    Build.VERSION_CODES.S_V2 -> 12

    Build.VERSION_CODES.TIRAMISU -> 13
    Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> 14
    Build.VERSION_CODES.VANILLA_ICE_CREAM -> 15

    else -> -1
}

@Composable
fun androidVersionErrorMessage(version: Int): String = buildString {
    append(stringResource(R.string.error_android_version_prefix))
    append(versionTranslate(version))
    append(stringResource(R.string.error_android_version_suffix))
}

// Android 12 Feature
val DYNAMIC_COLOR_SUPPORTED = notLessThan(Build.VERSION_CODES.S)

// Android 10 Feature
val AUTO_DARK_MODE_SUPPORTED = notLessThan(Build.VERSION_CODES.Q)