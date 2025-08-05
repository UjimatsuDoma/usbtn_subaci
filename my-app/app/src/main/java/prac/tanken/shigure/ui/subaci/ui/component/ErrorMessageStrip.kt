package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme

@Composable
fun ErrorMessageStrip(
    message: String,
    modifier: Modifier = Modifier
) = Card(modifier) {
    BasicListItem(
        icon = Icons.Default.Error,
        headline = message,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.error,
            leadingIconColor = MaterialTheme.colorScheme.onError,
            headlineColor = MaterialTheme.colorScheme.onError,
        )
    )
}

@Composable
fun InfoMessageStrip(
    message: String,
    modifier: Modifier = Modifier
) = Card(modifier) {
    BasicListItem(
        icon = Icons.Default.Info,
        headline = message,
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.tertiary,
            leadingIconColor = MaterialTheme.colorScheme.onTertiary,
            headlineColor = MaterialTheme.colorScheme.onTertiary,
        )
    )
}

@Preview
@Composable
private fun ErrorMessageStripPreview() {
    ShigureUiButtonAppComposeImplementationTheme {
        ErrorMessageStrip("这是一条错误消息。")
    }
}

@Preview
@Composable
private fun InfoMessageStripPreview() {
    ShigureUiButtonAppComposeImplementationTheme {
        InfoMessageStrip("这是一条提示消息。")
    }
}