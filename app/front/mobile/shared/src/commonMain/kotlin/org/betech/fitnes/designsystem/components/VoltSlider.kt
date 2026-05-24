package org.betech.fitnes.designsystem.components

import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.betech.fitnes.designsystem.color.VoltColors

/**
 * Volt slider. Thumb + active track = volt; inactive track = outline.
 */
@Composable
fun VoltSlider(
    value: Float,
    onValueChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    steps: Int = 0
) {
    Slider(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        enabled = enabled,
        valueRange = valueRange,
        steps = steps,
        colors = SliderDefaults.colors(
            thumbColor = VoltColors.volt,
            activeTrackColor = VoltColors.volt,
            inactiveTrackColor = VoltColors.outline,
            activeTickColor = VoltColors.onVolt,
            inactiveTickColor = VoltColors.outlineStrong,
            disabledThumbColor = VoltColors.surface2,
            disabledActiveTrackColor = VoltColors.surface2,
            disabledInactiveTrackColor = VoltColors.outline
        )
    )
}
