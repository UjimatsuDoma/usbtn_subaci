@file:OptIn(ExperimentalMaterial3Api::class)

package prac.tanken.shigure.ui.subaci.core.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun LoadingIndefinitelyScreen(
    modifier: Modifier = Modifier,
    title: String,
) {
    Column(modifier) {
        LargeTopAppBar(
            title = { Text(title) },
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}

@Composable
fun LoadingWithProgressScreen(
    modifier: Modifier = Modifier,
    title: String,
    progress: Float,
) {
    Column(modifier) {
        LargeTopAppBar(
            title = { Text(title) },
        )
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxSize(0.5f)
            )
        }
    }
}

@Preview
@Composable
private fun FontCheckingScreenPreview() {
    LoadingIndefinitelyScreen(title = "Loading...")
}