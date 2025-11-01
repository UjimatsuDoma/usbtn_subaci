package prac.tanken.shigure.ui.subaci.feature.base.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import prac.tanken.shigure.ui.subaci.core.ui.NotoSerifAutoLang
import prac.tanken.shigure.ui.subaci.feature.base.R
import prac.tanken.shigure.ui.subaci.core.ui.theme.ShigureUiButtonAppComposeImplementationTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(R.string.loading),
                fontWeight = FontWeight.Black,
                fontFamily = NotoSerifAutoLang
            )
        },
        modifier = modifier
    )
}

@Preview(locale = "zh")
@Composable
fun LoadingTopBarPreview(modifier: Modifier = Modifier) {
    ShigureUiButtonAppComposeImplementationTheme {
        LoadingTopBar()
    }
}