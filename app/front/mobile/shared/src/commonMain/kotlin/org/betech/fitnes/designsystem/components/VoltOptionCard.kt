package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Selectable option card — used by Q1 goal selection and reusable across
 * future single-choice question screens (Q2 Sex, Q3 Experience, etc.).
 *
 * Spec (Pencil S5QT23):
 *   • 77dp tall, full container width, 20dp corner radius
 *   • Selected: surface1 fill + 2dp volt stroke + trailing check icon
 *   • Unselected: surface1 fill + 1dp outline stroke
 *   • Leading icon slot (44×44dp tile) — caller-provided composable
 */
@Composable
fun VoltOptionCard(
    title: String,
    subtitle: String?,
    leadingIcon: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(20.dp)
    val borderWidth = if (selected) 2.dp else 1.dp
    val borderColor = if (selected) VoltColors.volt else VoltColors.outline

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(77.dp)
            .clip(shape)
            .background(VoltColors.surface1)
            .border(borderWidth, borderColor, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        leadingIcon()
        Spacer(Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = title,
                style = VoltType.labelLarge.copy(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = VoltColors.onSurface,
                maxLines = 1,
            )
            if (!subtitle.isNullOrEmpty()) {
                Text(
                    text = subtitle,
                    style = VoltType.bodyMedium.copy(fontSize = 13.sp),
                    color = VoltColors.onSurfaceMuted,
                    maxLines = 1,
                )
            }
        }
        if (selected) {
            Spacer(Modifier.width(12.dp))
            CheckGlyph()
        }
    }
}

/**
 * 44×44dp leading icon tile used by [VoltOptionCard]. Volt-tinted when the
 * card is selected, neutral `surface2` otherwise.
 */
@Composable
fun VoltOptionIconTile(
    selected: Boolean,
    content: @Composable () -> Unit,
) {
    val shape = RoundedCornerShape(16.dp)
    val bg = if (selected) VoltColors.volt else VoltColors.surface2
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(shape)
            .background(bg),
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

/** Small check glyph (vector-drawn) — trailing affordance on selected cards. */
@Composable
private fun CheckGlyph() {
    Box(
        modifier = Modifier
            .size(20.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(VoltColors.volt),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(12.dp)) {
            val s = size.minDimension
            val sw = s / 7f
            drawLine(
                color = VoltColors.onVolt,
                start = Offset(s * 0.18f, s * 0.55f),
                end = Offset(s * 0.42f, s * 0.78f),
                strokeWidth = sw,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = VoltColors.onVolt,
                start = Offset(s * 0.42f, s * 0.78f),
                end = Offset(s * 0.82f, s * 0.28f),
                strokeWidth = sw,
                cap = StrokeCap.Round,
            )
        }
    }
}
