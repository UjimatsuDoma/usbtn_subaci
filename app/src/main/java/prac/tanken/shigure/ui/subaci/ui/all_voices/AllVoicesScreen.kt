package prac.tanken.shigure.ui.subaci.ui.all_voices

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Casino
import androidx.compose.material3.Badge
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.VoiceReference
import prac.tanken.shigure.ui.subaci.ui.component.AdvancedButton
import prac.tanken.shigure.ui.subaci.ui.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.ui.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.ui.component.VoicesFlowRow
import prac.tanken.shigure.ui.subaci.ui.theme.NotoSansJP
import prac.tanken.shigure.ui.subaci.ui.theme.NotoSerifJP
import java.util.Locale
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
            LoadingScreenBody()
        } else {
            AllVoicesTopBar(
                onDailyVoice = viewModel::playDailyVoice
            )
            AllVoicesScreen(
                voices = viewModel.voices,
                onPlay = viewModel::onButtonClicked,
                onAddToPlaylist = viewModel::addToPlaylist,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllVoicesTopBar(
    modifier: Modifier = Modifier,
    onDailyVoice: () -> Unit,
) {
    TopAppBar(
        modifier = modifier,
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(TankenR.string.app_name),
                fontWeight = FontWeight.Bold,
                fontFamily = when(Locale.getDefault()) {
                    Locale.JAPAN -> NotoSerifJP
                    else -> FontFamily.Serif
                }
            )
        },
        actions = {
            IconButton(onClick = onDailyVoice) {
                Icon(
                    imageVector = Icons.Default.Casino,
                    contentDescription = stringResource(TankenR.string.daily_random_voice)
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
    Box(modifier) {
        VoicesFlowRow(
            voices = voices,
            modifier = modifier.verticalScroll(rememberScrollState()),
        ) { voice ->
            var expanded by remember { mutableStateOf(false) }

            Column {
                AdvancedButton(
                    onClick = { onPlay(voice.toReference()) },
                    onLongPress = { expanded = true },
                    contentFontFamily = NotoSansJP,
                ) {
                    Text(text = voice.label)
                    if (voice.new == true) {
                        Badge {
                            Text("NEW")
                        }
                    }
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Add to playlist") },
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
