package prac.tanken.shigure.ui.subaci.feature.settings.ui.items

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
import prac.tanken.shigure.ui.subaci.core.common.android.version.androidVersionErrorMessage
import prac.tanken.shigure.ui.subaci.feature.base.component.RadioButtonCard
import prac.tanken.shigure.ui.subaci.core.data.settings.AppSettings
import prac.tanken.shigure.ui.subaci.core.data.settings.ui.AppColor
import prac.tanken.shigure.ui.subaci.core.data.settings.ui.AppDarkMode
import prac.tanken.shigure.ui.subaci.core.data.settings.ui.NavigationLabelBehaviour
import prac.tanken.shigure.ui.subaci.feature.settings.R

fun LazyListScope.uiSettings(
    modifier: Modifier = Modifier,
    appSettings: AppSettings = AppSettings(),
    onUpdateAppSettings: (AppSettings) -> Unit = {},
) = item {
    Column(modifier) {
        val uiSettings = appSettings.uiSettings

        ListItem(
            headlineContent = {
                Text(
                    text = stringResource(R.string.settings_ui),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        )
        AppColorSetting(
            appColor = uiSettings.appColor,
            onUpdateAppColor = {
                onUpdateAppSettings(
                    appSettings.copy(
                        uiSettings = uiSettings.copy(
                            appColor = it
                        )
                    )
                )
            }
        )
        AppDarkModeSetting(
            appDarkMode = uiSettings.appDarkMode,
            onUpdateAppDarkMode = {
                onUpdateAppSettings(
                    appSettings.copy(
                        uiSettings = uiSettings.copy(
                            appDarkMode = it
                        )
                    )
                )
            }
        )
        NavigationLabelBehaviourSetting(
            navigationLabelBehaviour = uiSettings.bottomBarLabelBehaviour,
            onUpdateNavigationLabelBehaviour = {
                onUpdateAppSettings(
                    appSettings.copy(
                        uiSettings = uiSettings.copy(
                            bottomBarLabelBehaviour = it
                        )
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

        AnimatedVisibility(expanded) {
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
                    enabled = Build.VERSION.SDK_INT>= Build.VERSION_CODES.S,
                    onSelected = { if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.S) onUpdateAppColor(AppColor.Dynamic) },
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

//@Preview(apiLevel = Build.VERSION_CODES.Q)
//@Composable
//private fun AppColorSettingPreviewLegacy() {
//    Card {
//        AppColorSetting()
//    }
//}

@Composable
private fun ColumnScope.AppDarkModeSetting(
    modifier: Modifier = Modifier,
    appDarkMode: AppDarkMode = AppDarkMode.default,
    onUpdateAppDarkMode: (AppDarkMode) -> Unit = {},
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val dropdownIcon =
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

        AnimatedVisibility(expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .clip(CardDefaults.shape)
            ) {
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = appDarkMode == AppDarkMode.Dynamic,
                    enabled = Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q,
                    onSelected = { if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q) onUpdateAppDarkMode(AppDarkMode.Dynamic) },
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

//@Preview(apiLevel = Build.VERSION_CODES.P)
//@Composable
//private fun AppDarkModeSettingPreviewLegacy() {
//    Card {
//        AppDarkModeSetting()
//    }
//}

@Composable
private fun ColumnScope.NavigationLabelBehaviourSetting(
    modifier: Modifier = Modifier,
    navigationLabelBehaviour: NavigationLabelBehaviour = NavigationLabelBehaviour.default,
    onUpdateNavigationLabelBehaviour: (NavigationLabelBehaviour) -> Unit = {},
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
                        text = stringResource(navigationLabelBehaviour.displayName),
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

        AnimatedVisibility(expanded) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .height(IntrinsicSize.Max)
                    .clip(CardDefaults.shape)
            ) {
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = NavigationLabelBehaviour == NavigationLabelBehaviour.ShowAlways,
                    onSelected = {
                        onUpdateNavigationLabelBehaviour(NavigationLabelBehaviour.ShowAlways)
                    },
                    title = stringResource(NavigationLabelBehaviour.ShowAlways.displayName),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = NavigationLabelBehaviour == NavigationLabelBehaviour.ShowWhenSelected,
                    onSelected = {
                        onUpdateNavigationLabelBehaviour(NavigationLabelBehaviour.ShowWhenSelected)
                    },
                    title = stringResource(NavigationLabelBehaviour.ShowWhenSelected.displayName),
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                )
                RadioButtonCard(
                    shape = RoundedCornerShape(size = 4.dp),
                    selected = NavigationLabelBehaviour == NavigationLabelBehaviour.Hide,
                    onSelected = {
                        onUpdateNavigationLabelBehaviour(NavigationLabelBehaviour.Hide)
                    },
                    title = stringResource(NavigationLabelBehaviour.Hide.displayName),
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
private fun NavigationLabelBehaviourSettingPreview() {
    Card {
        NavigationLabelBehaviourSetting()
    }
}