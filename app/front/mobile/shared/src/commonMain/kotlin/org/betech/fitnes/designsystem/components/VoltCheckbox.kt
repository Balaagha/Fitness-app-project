package org.betech.fitnes.designsystem.components

import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.betech.fitnes.designsystem.color.VoltColors

/**
 * Volt checkbox — ux-phase3-design-system-spec §7 (option-multi).
 * Checked fill = volt; check icon = on-volt; unchecked border = outline.
 */
@Composable
fun VoltCheckbox(
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit)?,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Checkbox(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        enabled = enabled,
        colors = CheckboxDefaults.colors(
            checkedColor = VoltColors.volt,
            uncheckedColor = VoltColors.outline,
            checkmarkColor = VoltColors.onVolt,
            disabledCheckedColor = VoltColors.surface2,
            disabledUncheckedColor = VoltColors.surface2,
            disabledIndeterminateColor = VoltColors.surface2
        )
    )
}
