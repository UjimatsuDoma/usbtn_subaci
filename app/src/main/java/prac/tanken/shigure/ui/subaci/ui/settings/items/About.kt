package prac.tanken.shigure.ui.subaci.ui.settings.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import prac.tanken.shigure.ui.subaci.BuildConfig
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.ui.component.BasicListItem

fun LazyListScope.aboutSettings(
    modifier: Modifier = Modifier,
) = item {
    Column(modifier) {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.settings_about_title),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
        AboutThisApp()
        BuiltInColorSource()
        BuildTime()
    }
}

@Preview
@Composable
private fun AboutPreview() = Card {
    LazyColumn {
        aboutSettings()
    }
}

@Composable
private fun ColumnScope.AboutThisApp() = BasicListItem(
    icon = Icons.Outlined.Android,
    headline = stringResource(R.string.app_name),
    underline = BuildConfig.VERSION_NAME
)

@Composable
private fun ColumnScope.BuiltInColorSource() = BasicListItem(
    icon = Icons.Outlined.Image,
    headline = stringResource(R.string.app_built_in_color_source),
    underline = buildString {
        append(stringResource(R.string.app_built_in_color_source_message_prefix))
        append(BuildConfig.BUILT_IN_COLOR_SOURCE)
    }
)

@Composable
private fun BuildTime() = BasicListItem(
    icon = Icons.Outlined.Schedule,
    headline = stringResource(R.string.app_build_time),
    underline = BuildConfig.BUILD_TIME
)