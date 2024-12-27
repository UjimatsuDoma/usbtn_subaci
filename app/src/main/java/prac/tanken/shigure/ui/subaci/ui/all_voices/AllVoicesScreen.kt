package prac.tanken.shigure.ui.subaci.ui.all_voices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prac.tanken.shigure.ui.subaci.BuildConfig
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.data.util.CallbackInvokedAsIs
import prac.tanken.shigure.ui.subaci.ui.NotoSerifMultiLang
import prac.tanken.shigure.ui.subaci.ui.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.ui.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.ui.component.VoiceButton
import prac.tanken.shigure.ui.subaci.ui.component.VoicesFlowRow
import prac.tanken.shigure.ui.subaci.R as TankenR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllVoicesScreen(
    modifier: Modifier = Modifier,
    viewModel: AllVoicesViewModel,
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Column(modifier) {
        if (isLoading) {
            LoadingTopBar()
            LoadingScreenBody(modifier = modifier.weight(1f))
        } else {
            var test by remember { mutableStateOf(false) }

            AllVoicesTopBar(
                onDailyVoice = viewModel::playDailyVoice,
                test = { test = true }
            )
            AllVoicesScreen(
                voices = viewModel.voices,
                onPlay = viewModel::onButtonClicked,
                onAddToPlaylist = viewModel::addToPlaylist,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
            )
            if (test) {
                TestModal(testDismiss = { test = false })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllVoicesTopBar(
    modifier: Modifier = Modifier,
    onDailyVoice: () -> Unit,
    test: CallbackInvokedAsIs = {},
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(TankenR.string.app_name),
                fontWeight = FontWeight.Bold,
                fontFamily = NotoSerifMultiLang
            )
        },
        actions = {
            IconButton(onClick = onDailyVoice) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = stringResource(TankenR.string.daily_random_voice)
                )
            }
            IconButton(onClick = test) {
                Icon(
                    imageVector = Icons.Default.Info,
                    contentDescription = null
                )
            }
        }
    )
}

@Composable
internal fun AllVoicesScreen(
    voices: List<Voice>,
    onPlay: (VoiceReference) -> Unit,
    onAddToPlaylist: (VoiceReference) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopStart
    ) {
        VoicesFlowRow(
            voices = voices,
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) { voice ->
            var expanded by remember { mutableStateOf(false) }

            Column {
                VoiceButton(
                    voice = voice,
                    onPlay = onPlay,
                    onLongPress = { expanded = true },
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(TankenR.string.voices_add_to_playlist)) },
                        onClick = {
                            expanded = false
                            onAddToPlaylist(voice.toReference())
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TestDialog(
    modifier: Modifier = Modifier,
    testDismiss: CallbackInvokedAsIs,
) {
    Dialog(
        onDismissRequest = testDismiss
    ) {
        Card(
            modifier = Modifier
                .fillMaxHeight(0.5f),
        ) {
            Box(
                modifier = Modifier.padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = BuildConfig.VERSION_NAME,
                    style = MaterialTheme.typography.titleLarge
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TestModal(
    modifier: Modifier = Modifier,
    testDismiss: CallbackInvokedAsIs,
) {
    ModalBottomSheet(
        onDismissRequest = testDismiss
    ) {
        Box(
            modifier = Modifier.padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = BuildConfig.VERSION_NAME,
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
}
