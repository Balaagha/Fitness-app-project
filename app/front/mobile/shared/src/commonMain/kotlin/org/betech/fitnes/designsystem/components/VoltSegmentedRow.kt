package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.collections.immutable.ImmutableList
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Volt segmented row — equal-weight selectable chips that fill the available
 * row width. Introduced for Q7 (Pencil H0uZ0e) and promoted to a reusable
 * because schedule / day-count / preset selectors share this exact pattern.
 *
 * Doctrine:
 *  - Unselected: `surface1` fill + 1dp `outline` border + `onSurface` text.
 *  - Selected:   `volt` fill + `onVolt` text (NO border — flat at full chroma).
 *  - 56dp tall, 16dp corner radius, 8dp inter-chip gap.
 */
@Composable
fun <T : Any> VoltSegmentedRow(
    options: ImmutableList<T>,
    selected: T,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    label: (T) -> String = { it.toString() },
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEach { option ->
            val isSelected = option == selected
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (isSelected) VoltColors.volt else VoltColors.surface1)
                    .then(
                        if (isSelected) Modifier
                        else Modifier.border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp))
                    )
                    .clickable { onSelected(option) },
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = label(option),
                    style = VoltType.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = if (isSelected) VoltColors.onVolt else VoltColors.onSurface,
                )
            }
        }
    }
}
