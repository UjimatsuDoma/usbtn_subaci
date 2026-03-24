@file:OptIn(ExperimentalMaterial3Api::class)

package prac.tanken.shigure.ui.subaci.core.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp

@Composable
fun ProceedOrBackScreen(
    modifier: Modifier = Modifier,
    title: String,
    content: String,
    onProceed: () -> Unit,
    onBack: () -> Unit,
) {
    Column(modifier) {
        LargeTopAppBar(
            title = { Text(title) },
        )
        Box(
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .fillMaxWidth(),
        ) {
            Text(content, Modifier.align(Alignment.TopStart))
            Row(Modifier.align(Alignment.BottomEnd)) {
                OutlinedButton(onBack) { Text("Back") }
                Spacer(Modifier.size(16.dp))
                Button(onProceed) { Text("Proceed") }
            }
        }
    }
}

@Preview
@Composable
private fun ProceedOrBackScreenPreview() {
    ProceedOrBackScreen(
        title = "Step 99999",
        content = LoremIpsum(20).values.joinToString(" "),
        onProceed = {},
        onBack = {}
    )
}