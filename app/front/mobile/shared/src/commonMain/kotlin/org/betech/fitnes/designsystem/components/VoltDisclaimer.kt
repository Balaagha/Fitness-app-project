package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.spacing.VoltSpacing
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Disclaimer / notice block kinds — ux-phase3-design-system-spec §8.2.
 */
enum class DisclaimerKind {
    /** Apple 2025 AI Disclosure copy — surface-2 bg, volt left accent. */
    AI_DISCLOSURE,

    /** Pregnancy / postpartum hard-stop — surface-2 bg, danger left accent. */
    MEDICAL_HARD_STOP,

    /** Generic informational note — outline border, muted text. */
    INFO
}

/**
 * Volt disclaimer / notice card.
 *
 * Visual contract:
 *  - AI_DISCLOSURE   : surface-2 bg, on-surface text, 4dp volt left accent bar
 *  - MEDICAL_HARD_STOP: surface-2 bg, on-surface text, 4dp danger left accent bar
 *  - INFO            : transparent bg, 1dp outline border, on-surface-muted text
 */
@Composable
fun VoltDisclaimer(
    text: String,
    kind: DisclaimerKind,
    modifier: Modifier = Modifier
) {
    when (kind) {
        DisclaimerKind.AI_DISCLOSURE -> AccentBox(
            text = text,
            accent = VoltColors.volt,
            background = VoltColors.surface2,
            textColor = VoltColors.onSurface,
            modifier = modifier
        )

        DisclaimerKind.MEDICAL_HARD_STOP -> AccentBox(
            text = text,
            accent = VoltColors.danger,
            background = VoltColors.surface2,
            textColor = VoltColors.onSurface,
            modifier = modifier
        )

        DisclaimerKind.INFO -> Box(
            modifier = modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .border(1.dp, VoltColors.outline, RoundedCornerShape(12.dp))
                .padding(VoltSpacing.lg)
        ) {
            Text(text, style = VoltType.bodyMedium, color = VoltColors.onSurfaceMuted)
        }
    }
}

@Composable
private fun AccentBox(
    text: String,
    accent: Color,
    background: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(background)
            .height(IntrinsicSize.Min)
    ) {
        // Left accent bar — fills the row height.
        Box(
            modifier = Modifier
                .width(4.dp)
                .fillMaxHeight()
                .background(accent)
        )
        Box(modifier = Modifier.padding(VoltSpacing.lg)) {
            Text(text, style = VoltType.bodyMedium, color = textColor)
        }
    }
}
