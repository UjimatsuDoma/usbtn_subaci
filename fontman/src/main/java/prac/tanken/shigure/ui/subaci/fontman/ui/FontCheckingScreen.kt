@file:OptIn(ExperimentalMaterial3Api::class)

package prac.tanken.shigure.ui.subaci.fontman.ui

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prac.tanken.shigure.ui.subaci.core.ui.screen.LargeLoadingIndefinitelyScreen
import prac.tanken.shigure.ui.subaci.fontman.domain.FontCheckingState

@Composable
fun FontCheckingScreen(
    modifier: Modifier = Modifier,
    viewModel: FontManagementViewModel,
    onPassed: () -> Unit,
    toDecompress: () -> Unit,
    toInstantiate: () -> Unit,
) {
    val cState by viewModel.cState.collectAsStateWithLifecycle()

    when (cState) {
        FontCheckingState.Checking -> {
            LargeLoadingIndefinitelyScreen(modifier, "Checking fonts...")
        }

        FontCheckingState.Passed -> {
            onPassed()
        }

        FontCheckingState.NeedDecompress -> {
            toDecompress()
        }

        FontCheckingState.NeedInstantiate -> {
            toInstantiate()
        }

        FontCheckingState.Cleaning -> {
            LargeLoadingIndefinitelyScreen(modifier, "Cleaning files...")
        }
    }
}



