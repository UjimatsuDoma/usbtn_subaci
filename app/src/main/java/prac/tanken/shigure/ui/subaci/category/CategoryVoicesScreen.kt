package prac.tanken.shigure.ui.subaci.category

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.components.VoicesList
import prac.tanken.shigure.ui.subaci.model.Category
import prac.tanken.shigure.ui.subaci.model.Voice

@Composable
fun CategoryVoicesScreen(
    categories: Array<Category>,
    voices: Array<Voice>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        var selected by remember { mutableStateOf(categories[0]) }
        var isLoading by remember { mutableStateOf(true) }
        var categoryVoices by remember { mutableStateOf<Array<Voice>>(emptyArray()) }

        LaunchedEffect(selected) {
            isLoading = true
            categoryVoices = voices.filter { it.id in selected.idList.map { it.id } }.toTypedArray()
            isLoading = false
        }

        Column {
            var expanded by remember { mutableStateOf(false) }

            Button(onClick = { expanded = !expanded }) {
                Text(
                    text = selected.className
                )
            }
            DropdownMenu(
                expanded = expanded,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(200.dp),
                onDismissRequest = {}
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        onClick = {
                            selected = category
                            expanded = !expanded
                        },
                        text = {
                            Text(
                                text = category.className
                            )
                        }
                    )
                }
            }
        }

        if (!isLoading) {
            VoicesList(
                voices = categoryVoices,
                modifier = Modifier.weight(1f)
            )
        } else {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}