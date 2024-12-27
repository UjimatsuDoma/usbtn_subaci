package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import prac.tanken.shigure.ui.subaci.ui.NotoSerifMultiLang
import prac.tanken.shigure.ui.subaci.ui.theme.ShigureUiButtonAppComposeImplementationTheme
import java.util.Locale
import prac.tanken.shigure.ui.subaci.R as TankenR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingTopBar(modifier: Modifier = Modifier) {
    val titleFontFamily = NotoSerifMultiLang

    LaunchedEffect(Unit) {
        println(Locale.getDefault())
        println(Locale.SIMPLIFIED_CHINESE.country)
    }

    TopAppBar(
        windowInsets = WindowInsets(0),
        title = {
            Text(
                text = stringResource(TankenR.string.loading),
                fontWeight = FontWeight.Bold,
                fontFamily = titleFontFamily
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