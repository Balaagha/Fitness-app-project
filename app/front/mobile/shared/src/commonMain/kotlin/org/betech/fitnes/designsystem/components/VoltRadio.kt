package org.betech.fitnes.designsystem.components

import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.betech.fitnes.designsystem.color.VoltColors

/**
 * Volt radio button — ux-phase3-design-system-spec §7 (option).
 * Selected = volt; unselected = outline.
 */
@Composable
fun VoltRadio(
    selected: Boolean,
    onClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    RadioButton(
        selected = selected,
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = RadioButtonDefaults.colors(
            selectedColor = VoltColors.volt,
            unselectedColor = VoltColors.outline,
            disabledSelectedColor = VoltColors.surface2,
            disabledUnselectedColor = VoltColors.surface2
        )
    )
}
