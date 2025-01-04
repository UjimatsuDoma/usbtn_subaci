package prac.tanken.shigure.ui.subaci.ui.category

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import prac.tanken.shigure.ui.subaci.data.model.voices.CategoryVO
import prac.tanken.shigure.ui.subaci.data.model.Voice
import prac.tanken.shigure.ui.subaci.data.model.voices.VoiceReference
import prac.tanken.shigure.ui.subaci.ui.NotoSansJP
import prac.tanken.shigure.ui.subaci.ui.NotoSerifJP
import prac.tanken.shigure.ui.subaci.ui.component.LoadingScreenBody
import prac.tanken.shigure.ui.subaci.ui.component.LoadingTopBar
import prac.tanken.shigure.ui.subaci.ui.component.VoiceButton
import prac.tanken.shigure.ui.subaci.ui.component.VoicesFlowRow
import com.microsoft.fluent.mobile.icons.R as FluentR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: CategoryViewModel,
) {
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    Column(modifier.fillMaxWidth()) {
        if (isLoading) {
            LoadingTopBar()
            LoadingScreenBody(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(1f)
            )
        } else {
            val selectedIndex by viewModel.selectedIndex.collectAsStateWithLifecycle()
            val selectedVoices = viewModel.selectedVoices.value

            CategoryTopBar(
                categories = viewModel.categoriesUi,
                selectedIndex = selectedIndex,
                onCategoryChanged = viewModel::selectByIndex
            )
            CategoryScreen(
                voices = selectedVoices,
                onPlay = viewModel::onButtonClicked,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryTopBar(
    categories: List<CategoryVO>,
    selectedIndex: Int,
    onCategoryChanged: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        windowInsets = WindowInsets(0),
        modifier = modifier,
        title = {
            Column {
                var expanded by rememberSaveable { mutableStateOf(false) }
                val trailingIcon = if (expanded) {
                    FluentR.drawable.ic_fluent_caret_up_20_filled
                } else {
                    FluentR.drawable.ic_fluent_caret_down_20_filled
                }

                Row(
                    modifier = Modifier.clickable { expanded = true },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = categories[selectedIndex].className,
                        fontFamily = NotoSerifJP,
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        painter = painterResource(trailingIcon),
                        contentDescription = null
                    )
                }
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.height(200.dp)
                ) {
                    categories.forEachIndexed { index, category ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = category.className,
                                    fontFamily = NotoSansJP
                                )
                            },
                            onClick = {
                                onCategoryChanged(index)
                                expanded = false
                            }
                        )
                    }
                }
            }
        },
    )
}

@Composable
internal fun CategoryScreen(
    voices: List<Voice>,
    onPlay: (VoiceReference) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier) {
        VoicesFlowRow(
            voices = voices,
            Modifier.verticalScroll(rememberScrollState())
        ) { voice ->
            VoiceButton(
                voice = voice,
                onPlay = onPlay
            )
        }
    }
}