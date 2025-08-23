package prac.tanken.shigure.ui.subaci.base.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Preview
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
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
    colors: ListItemColors = ListItemDefaults.colors(),
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
                style = MaterialTheme.typography.titleMedium
            )
        }
    },
    headlineContent = {
        Text(
            text = headline,
            style = MaterialTheme.typography.bodyLarge
        )
    },
    supportingContent = {
        underline?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    },
    colors = colors,
    modifier = modifier
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