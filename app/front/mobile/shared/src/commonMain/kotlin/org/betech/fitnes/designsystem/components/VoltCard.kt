package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors

/**
 * Volt card — ux-phase3-design-system-spec §9 (principle card pattern).
 * Surface-1 background, 20dp corner, optional 1dp outline border.
 */
@Composable
fun VoltCard(
    modifier: Modifier = Modifier,
    bordered: Boolean = true,
    onClick: (() -> Unit)? = null,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    val colors = CardDefaults.cardColors(
        containerColor = VoltColors.surface1,
        contentColor = VoltColors.onSurface
    )
    val border = if (bordered) BorderStroke(1.dp, VoltColors.outline) else null

    if (onClick != null) {
        Card(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = colors,
            border = border
        ) {
            Box(Modifier.padding(contentPadding)) { content() }
        }
    } else {
        Card(
            modifier = modifier,
            shape = shape,
            colors = colors,
            border = border
        ) {
            Box(Modifier.padding(contentPadding)) { content() }
        }
    }
}
