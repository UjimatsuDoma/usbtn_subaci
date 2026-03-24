package prac.tanken.shigure.ui.subaci.fontman.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prac.tanken.shigure.ui.subaci.core.ui.screen.LoadingWithProgressScreen
import prac.tanken.shigure.ui.subaci.core.ui.screen.ProceedOrBackScreen
import prac.tanken.shigure.ui.subaci.fontman.domain.FontDecompressState
import prac.tanken.shigure.ui.subaci.fontman.domain.FontInstantiateState

@Composable
fun FontInstantiateScreen(
    modifier: Modifier = Modifier,
    viewModel: FontManagementViewModel,
    onBack: () -> Unit,
    onPassed: () -> Unit,
) {
    val iState by viewModel.iState.collectAsStateWithLifecycle()

    when (iState) {
        FontInstantiateState.Complete -> {
            viewModel.onInstantiateComplete()
            onPassed()
        }

        FontInstantiateState.Failed -> {
            onBack()
        }

        FontInstantiateState.Intro -> {
            ProceedOrBackScreen(
                title = "Static font not ready",
                content = """
                    After extracting the variable fonts,
                    they need to be converted to static fonts
                    to be used on systems below Android 8.
                """.trimIndent(),
                onProceed = { viewModel.gotoInstantiate() },
                onBack = onBack
            )
        }

        is FontInstantiateState.Progress -> {
            LoadingWithProgressScreen(
                modifier,
                "Instantiating",
                (iState as FontInstantiateState.Progress).percentage
            )
        }
    }

}