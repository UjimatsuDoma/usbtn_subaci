package prac.tanken.shigure.ui.subaci.core.common.android.version

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import prac.tanken.shigure.ui.subaci.core.common.R

@Composable
fun androidVersionErrorMessage(version: Int): String = buildString {
    append(stringResource(R.string.error_android_version_prefix))
    append(versionTranslate(version))
    append(stringResource(R.string.error_android_version_suffix))
}