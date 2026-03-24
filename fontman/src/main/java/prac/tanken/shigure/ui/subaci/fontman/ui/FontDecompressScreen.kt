package prac.tanken.shigure.ui.subaci.fontman.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prac.tanken.shigure.ui.subaci.core.ui.screen.LoadingWithProgressScreen
import prac.tanken.shigure.ui.subaci.core.ui.screen.ProceedOrBackScreen
import prac.tanken.shigure.ui.subaci.fontman.domain.FontDecompressState

@Composable
fun FontDecompressingScreen(
    modifier: Modifier = Modifier,
    viewModel: FontManagementViewModel,
    onBack: () -> Unit,
    onPassed: () -> Unit,
) {
    val dState by viewModel.dState.collectAsStateWithLifecycle()

    when (dState) {
        FontDecompressState.Complete -> {
            viewModel.onDecompressComplete()
            onPassed()
        }

        FontDecompressState.Failed -> {
            // TODO: a more sensible feedback
            onBack()
        }

        FontDecompressState.Intro -> {
            ProceedOrBackScreen(
                title = "Variable font not ready",
                content = """
                    Noto CJK Sans and Serif fonts need to be extracted inside the app's 
                    internal storage to be used as the default font of this app.
                """.trimIndent(),
                onProceed = { viewModel.gotoDecompress() },
                onBack = onBack
            )
        }

        is FontDecompressState.Progress -> {
            LoadingWithProgressScreen(
                modifier,
                "Decompressing",
                (dState as FontDecompressState.Progress).percentage
            )
        }
    }
}