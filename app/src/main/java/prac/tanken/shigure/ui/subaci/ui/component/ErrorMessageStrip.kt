package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme

@Composable
fun ErrorMessageStrip(
    message: String,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError
        ),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Top,
            content = {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = null
                )
                Text(
                    text = message,
                    modifier = Modifier.weight(1f)
                )
            }
        )
    }
}

@Preview
@Composable
private fun ErrorMessageStripPreview(modifier: Modifier = Modifier) {
    ShigureUiButtonAppComposeImplementationTheme {
        ErrorMessageStrip("这是一条错误消息。")
    }
}