package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Volt chip — ux-phase3-design-system-spec §7 (Chip block).
 * Pill shape. Unselected: surface-1 + muted text. Selected: volt + on-volt.
 */
@Composable
fun VoltChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        modifier = modifier.heightIn(min = 32.dp),
        enabled = enabled,
        label = { Text(text, style = VoltType.labelSmall) },
        shape = RoundedCornerShape(percent = 50),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = VoltColors.surface1,
            labelColor = VoltColors.onSurfaceMuted,
            selectedContainerColor = VoltColors.volt,
            selectedLabelColor = VoltColors.onVolt,
            disabledContainerColor = VoltColors.surface1,
            disabledLabelColor = VoltColors.onSurfaceMuted
        ),
        border = if (selected) {
            BorderStroke(0.dp, VoltColors.volt)
        } else {
            BorderStroke(1.dp, VoltColors.outline)
        }
    )
}
