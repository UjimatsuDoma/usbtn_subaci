package prac.tanken.shigure.ui.subaci.ui.settings.items

import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Label
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.R
import prac.tanken.shigure.ui.subaci.data.util.AUTO_DARK_MODE_SUPPORTED
import prac.tanken.shigure.ui.subaci.data.util.DYNAMIC_COLOR_SUPPORTED
import prac.tanken.shigure.ui.subaci.data.util.androidVersionErrorMessage
import prac.tanken.shigure.ui.subaci.ui.app.AppColor
import prac.tanken.shigure.ui.subaci.ui.app.AppDarkMode
import prac.tanken.shigure.ui.subaci.ui.app.AppSettings
import prac.tanken.shigure.ui.subaci.ui.app.BottomBarLabelBehaviour
import prac.tanken.shigure.ui.subaci.ui.component.RadioButtonCard

fun LazyListScope.uiSettings(
    modifier: Modifier = Modifier,
    appSettings: AppSettings = AppSettings(),
    onUpdateAppSettings: (AppSettings) -> Unit = {},
) = item {
    Column(modifier) {
        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.settings_ui),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
        AppColorSetting(
            appColor = appSettings.appColor,
            onUpdateAppColor = { onUpdateAppSettings(appSettings.copy(appColor = it)) }
        )
        AppDarkModeSetting(
            appDarkMode = appSettings.appDarkMode,
            onUpdateAppDarkMode = { onUpdateAppSettings(appSettings.copy(appDarkMode = it)) }
        )
        BottomBarLabelBehaviourSetting(
            bottomBarLabelBehaviour = appSettings.bottomBarLabelBehaviour,
            onUpdateBottomBarLabelBehaviour = {
                onUpdateAppSettings(
                    appSettings.copy(
                        bottomBarLabelBehaviour = it
                    )
                )
            }
        )
    }
}

@Preview
@Composable
private fun UiSettingsPreview() = Card {
    LazyColumn {
        uiSettings()
    }
}

@Composable
private fun ColumnScope.AppColorSetting(
    modifier: Modifier = Modifier,
    appColor: AppColor = AppColor.default,
    onUpdateAppColor: (AppColor) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var dropdownIcon =
        if (expanded) Icons.Default.ArrowDropUp
        else Icons.Default.ArrowDropDown

    Column(modifier) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Outlined.Palette,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text(
                    text = stringResource(R.string.settings_ui_app_color),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(appColor.displayName),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = dropdownIcon,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .clickable { expanded = !expanded }
        )

        AnimatedVisibility (expanded) {
            Row(
                modifier = Modifier
                    .height(intrinsicSize = IntrinsicSize.Max)
                    .padding(horizontal = 16.dp)
                    .clip(CardDefaults.shape),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.Top
            ) {
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    selected = appColor == AppColor.Dynamic,
                    enabled = DYNAMIC_COLOR_SUPPORTED,
                    onSelected = { if (DYNAMIC_COLOR_SUPPORTED) onUpdateAppColor(AppColor.Dynamic) },
                    title = stringResource(AppColor.Dynamic.displayName),
                    disabledMessage = androidVersionErrorMessage(Build.VERSION_CODES.S)
                )
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize(),
                    selected = appColor == AppColor.Static,
                    onSelected = { onUpdateAppColor(AppColor.Static) },
                    title = stringResource(AppColor.Static.displayName),
                )
            }
        }
    }
}

@Preview
@Composable
private fun AppColorSettingPreview() {
    Card {
        AppColorSetting()
    }
}

@Composable
private fun ColumnScope.AppDarkModeSetting(
    modifier: Modifier = Modifier,
    appDarkMode: AppDarkMode = AppDarkMode.default,
    onUpdateAppDarkMode: (AppDarkMode) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var dropdownIcon =
        if (expanded) Icons.Default.ArrowDropUp
        else Icons.Default.ArrowDropDown

    Column(modifier) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.Filled.Brightness4,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text(
                    text = stringResource(R.string.settings_ui_app_dark_mode),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(appDarkMode.displayName),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = dropdownIcon,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .clickable { expanded = !expanded }
        )

        AnimatedVisibility (expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(CardDefaults.shape)
            ) {
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = appDarkMode == AppDarkMode.Dynamic,
                    enabled = AUTO_DARK_MODE_SUPPORTED,
                    onSelected = { if (AUTO_DARK_MODE_SUPPORTED) onUpdateAppDarkMode(AppDarkMode.Dynamic) },
                    title = stringResource(AppDarkMode.Dynamic.displayName),
                    disabledMessage = androidVersionErrorMessage(Build.VERSION_CODES.Q)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.height(IntrinsicSize.Max)
                ) {
                    RadioButtonCard(
                        shape = RoundedCornerShape(size = 4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        selected = appDarkMode == AppDarkMode.Static(true),
                        onSelected = { onUpdateAppDarkMode(AppDarkMode.Static(true)) },
                        title = stringResource(AppDarkMode.Static(true).displayName),
                    )
                    RadioButtonCard(
                        shape = RoundedCornerShape(size = 4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        selected = appDarkMode == AppDarkMode.Static(false),
                        onSelected = { onUpdateAppDarkMode(AppDarkMode.Static(false)) },
                        title = stringResource(AppDarkMode.Static(false).displayName),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AppDarkModeSettingPreview() {
    Card {
        AppDarkModeSetting()
    }
}

@Composable
private fun ColumnScope.BottomBarLabelBehaviourSetting(
    modifier: Modifier = Modifier,
    bottomBarLabelBehaviour: BottomBarLabelBehaviour = BottomBarLabelBehaviour.default,
    onUpdateBottomBarLabelBehaviour: (BottomBarLabelBehaviour) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    var dropdownIcon =
        if (expanded) Icons.Default.ArrowDropUp
        else Icons.Default.ArrowDropDown

    Column(modifier) {
        ListItem(
            leadingContent = {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Label,
                    contentDescription = null
                )
            },
            headlineContent = {
                Text(
                    text = stringResource(R.string.settings_ui_bottom_bar_label_behaviour),
                    style = MaterialTheme.typography.bodyLarge
                )
            },
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = stringResource(bottomBarLabelBehaviour.displayName),
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Icon(
                        imageVector = dropdownIcon,
                        contentDescription = null
                    )
                }
            },
            modifier = Modifier
                .clickable { expanded = !expanded }
        )

        AnimatedVisibility (expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(IntrinsicSize.Max)
                    .clip(CardDefaults.shape)
            ) {
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = bottomBarLabelBehaviour == BottomBarLabelBehaviour.ShowAlways,
                    onSelected = {
                        onUpdateBottomBarLabelBehaviour(BottomBarLabelBehaviour.ShowAlways)
                    },
                    title = stringResource(BottomBarLabelBehaviour.ShowAlways.displayName),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = bottomBarLabelBehaviour == BottomBarLabelBehaviour.ShowWhenSelected,
                    onSelected = {
                        onUpdateBottomBarLabelBehaviour(BottomBarLabelBehaviour.ShowWhenSelected)
                    },
                    title = stringResource(BottomBarLabelBehaviour.ShowWhenSelected.displayName),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = bottomBarLabelBehaviour == BottomBarLabelBehaviour.Hide,
                    onSelected = {
                        onUpdateBottomBarLabelBehaviour(BottomBarLabelBehaviour.Hide)
                    },
                    title = stringResource(BottomBarLabelBehaviour.Hide.displayName),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
            }
        }
    }
}

@Preview
@Composable
private fun BottomBarLabelBehaviourSettingPreview() {
    Card {
        BottomBarLabelBehaviourSetting()
    }
}