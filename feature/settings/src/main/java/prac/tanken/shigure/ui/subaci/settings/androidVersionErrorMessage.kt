package prac.tanken.shigure.ui.subaci.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import prac.tanken.shigure.ui.subaci.common_res.R as CommonR

@Composable
fun androidVersionErrorMessage(version: Int): String = buildString {
    append(stringResource(CommonR.string.error_android_version_prefix))
    append(versionTranslate(version))
    append(stringResource(CommonR.string.error_android_version_suffix))
}