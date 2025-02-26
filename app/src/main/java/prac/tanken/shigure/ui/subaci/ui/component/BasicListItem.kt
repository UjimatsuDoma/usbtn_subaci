package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun BasicListItem(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    overLine: String? = null,
    headline: String,
    underline: String? = null,
) = ListItem(
    leadingContent = {
        icon?.let {
            Icon(
                imageVector = it,
                contentDescription = null
            )
        }
    },
    overlineContent = {
        overLine?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.titleSmall
            )
        }
    },
    headlineContent = {
        Text(
            text = headline,
            style = MaterialTheme.typography.bodyMedium
        )
    },
    supportingContent = {
        underline?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
)

@Preview
@Composable
private fun BasicListItemPreview() = Card {
    BasicListItem(
        icon = Icons.Default.Preview,
        overLine = "this is over line",
        headline = "this is headline",
        underline = "this is underline"
    )
}