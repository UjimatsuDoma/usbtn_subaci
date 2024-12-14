package prac.tanken.shigure.ui.subaci.ui.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontFamily

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AdvancedButton(
    onClick: () -> Unit,
    onLongPress: (() -> Unit)? = null,
    onDoubleClick: (() -> Unit)? = null,
    contentFontFamily: FontFamily,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(25),
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit,
) {
    val containerColor = if(enabled) colors.containerColor else colors.disabledContainerColor
    val contentColor = if(enabled) colors.contentColor else colors.disabledContentColor

    Surface(
        modifier = modifier
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongPress,
                onDoubleClick = onDoubleClick,
                enabled = enabled
            )
            .semantics { role = Role.Button },
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
        border = border,
    ) {
        CompositionLocalProvider(
            LocalContentColor provides contentColor,
            LocalTextStyle provides LocalTextStyle.current.merge(
                MaterialTheme.typography.labelLarge
            ).copy(fontFamily = contentFontFamily)
        ) {
            Row(
                Modifier.defaultMinSize(
                    minWidth = ButtonDefaults.MinWidth,
                    minHeight = ButtonDefaults.MinHeight
                ).padding(contentPadding),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                content = content
            )
        }
    }
}
