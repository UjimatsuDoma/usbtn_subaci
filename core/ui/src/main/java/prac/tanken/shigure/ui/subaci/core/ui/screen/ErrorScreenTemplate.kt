@file:OptIn(ExperimentalMaterial3Api::class)

package prac.tanken.shigure.ui.subaci.core.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import prac.tanken.shigure.ui.subaci.core.common.R
import prac.tanken.shigure.ui.subaci.core.ui.NotoSansMono

@Composable
fun ErrorScreen(
    message: String?,
    stackTrace: String,
    modifier: Modifier = Modifier,
    title: String = stringResource(R.string.error_generic),
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar ={
            TopAppBar(
                title = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            )
        }
    ) { innerPadding->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black)
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides LocalTextStyle.current
                    .copy(
                        fontFamily = NotoSansMono,
                        color = Color.White
                    )
            ) {
                Text(
                    text = buildAnnotatedString {
                        message?.let {
                            withStyle(
                                style = LocalTextStyle.current
                                    .toSpanStyle()
                                    .copy(
                                        fontWeight = FontWeight.Bold
                                    )
                            ) {
                                appendLine(message)
                            }
                        }
                        appendLine(stackTrace)
                    }
                )
            }
        }
    }
}

@Preview
@Composable
private fun ErrorScreenPreview() {
    val error = IllegalStateException("Test Exception")
    ErrorScreen(
        message = error.message,
        stackTrace = error.stackTraceToString()
    )
}