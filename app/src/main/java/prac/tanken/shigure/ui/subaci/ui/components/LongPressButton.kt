package prac.tanken.shigure.ui.subaci.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LongPressButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    content: @Composable RowScope.() -> Unit
) {
    Surface(
        color = ButtonDefaults.buttonColors().containerColor,
        modifier = modifier
            .semantics { role = Role.Button }
            .clip(RoundedCornerShape(50))
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .padding(ButtonDefaults.ContentPadding),
            content = content
        )
    }
}