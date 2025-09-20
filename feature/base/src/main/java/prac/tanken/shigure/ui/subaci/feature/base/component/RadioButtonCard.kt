package prac.tanken.shigure.ui.subaci.feature.base.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.data.util.CallbackInvokedAsIs

@Composable
fun RadioButtonCard(
    modifier: Modifier = Modifier,
    shape: Shape = CardDefaults.shape,
    selected: Boolean = false,
    enabled: Boolean = true,
    onSelected: CallbackInvokedAsIs = {},
    title: String = "This is title",
    disabledMessage: String = """
        This feature is not available on Android X.
        
    """.trimIndent()
) = Card(
    shape = shape,
    colors = CardDefaults.cardColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ),
    modifier = modifier
        .clickable { if (enabled) onSelected() }
) {
    Box(
        contentAlignment = Alignment.Center,
        content = {
            ListItem(
                leadingContent = {
                    RadioButton(
                        selected = selected,
                        enabled = enabled,
                        onClick = null,
                    )
                },
                headlineContent = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                supportingContent = {
                    if (!enabled) {
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer
                            ),
                            modifier = Modifier.padding(top = 8.dp),
                            content = {
                                Text(
                                    text = disabledMessage,
                                    style = MaterialTheme.typography.bodyMedium,
                                    modifier = Modifier.padding(8.dp)
                                )
                            }
                        )
                    }
                },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    )
}

@Preview
@Composable
private fun RadioButtonCardPreview() {
    RadioButtonCard(
        modifier = Modifier.wrapContentSize()
    )
}

@Preview
@Composable
private fun RadioButtonCardDisabledPreview() {
    RadioButtonCard(enabled = false)
}