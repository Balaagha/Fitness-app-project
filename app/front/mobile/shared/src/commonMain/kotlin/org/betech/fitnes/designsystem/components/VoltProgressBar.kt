package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors

/**
 * Linear progress bar for multi-step flows.
 * Used in VoltAppBar (onboarding 1/7 style) and standalone.
 *
 * @param progress 0f..1f.
 * @param total optional total step count (kept for future a11y semantics).
 */
@Composable
fun VoltProgressBar(
    progress: Float,
    modifier: Modifier = Modifier,
    @Suppress("UNUSED_PARAMETER") total: Int? = null
) {
    val clamped = progress.coerceIn(0f, 1f)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(6.dp)
            .clip(RoundedCornerShape(percent = 50))
            .background(VoltColors.outline)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(clamped)
                .fillMaxHeight()
                .background(VoltColors.volt)
        )
    }
}
