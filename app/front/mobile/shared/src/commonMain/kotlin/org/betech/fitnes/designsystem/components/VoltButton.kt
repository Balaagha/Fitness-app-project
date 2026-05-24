package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Volt button — 3 variants per ux-phase3-design-system-spec §6.
 *
 * Doctrine:
 *  - Primary background = `volt`, content = `onVolt` (black). White-on-volt is FORBIDDEN.
 *  - Min height 56dp, corner radius 16dp, full width by default.
 */
private val ButtonShape = RoundedCornerShape(16.dp)
private val ButtonMinHeight = 56.dp
private val ButtonContentPadding = PaddingValues(horizontal = 24.dp, vertical = 14.dp)

@Composable
fun VoltButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ButtonMinHeight)
            .defaultMinSize(minHeight = ButtonMinHeight),
        enabled = enabled,
        shape = ButtonShape,
        contentPadding = ButtonContentPadding,
        colors = ButtonDefaults.buttonColors(
            containerColor = VoltColors.volt,
            contentColor = VoltColors.onVolt,
            disabledContainerColor = VoltColors.surface2,
            disabledContentColor = VoltColors.onSurfaceMuted
        )
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}

@Composable
fun VoltButtonSecondary(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ButtonMinHeight)
            .defaultMinSize(minHeight = ButtonMinHeight),
        enabled = enabled,
        shape = ButtonShape,
        contentPadding = ButtonContentPadding,
        border = BorderStroke(1.dp, VoltColors.outline),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = VoltColors.surface1,
            contentColor = VoltColors.onSurface,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = VoltColors.onSurfaceMuted
        )
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}

@Composable
fun VoltButtonGhost(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = ButtonMinHeight)
            .defaultMinSize(minHeight = ButtonMinHeight),
        enabled = enabled,
        shape = ButtonShape,
        contentPadding = ButtonContentPadding,
        colors = ButtonDefaults.textButtonColors(
            contentColor = VoltColors.volt,
            disabledContentColor = VoltColors.onSurfaceMuted
        )
    ) {
        Text(text = text, style = VoltType.labelLarge)
    }
}
