package org.betech.fitnes.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.collections.immutable.ImmutableList
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Reusable measurement card used by Q4 (height + weight) and (forthcoming)
 * weight-tracking surfaces.
 *
 * Layout:
 *   [ icon ] [ caption + value + suffix ]            [ unit pill | unit pill ]
 *
 * `value` is tappable (opens an input dialog managed by the caller).
 * `unitOptions` renders 2..N stacked pills; the selected one is volt-filled
 * (onVolt text), unselected ones surface2 + onSurfaceMuted text.
 *
 * Pencil reference: qXLw8 ("Q4 · Boy + Çəki").
 */
@Composable
fun VoltMeasurementCard(
    leadingIcon: @Composable () -> Unit,
    caption: String,
    value: String,
    valueSuffix: String,
    unitOptions: ImmutableList<String>,
    selectedUnitIndex: Int,
    onUnitSelected: (Int) -> Unit,
    onValueTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(20.dp))
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        // ── Left: icon + (caption / value + suffix) ────────────────────
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(VoltColors.surface2),
                contentAlignment = Alignment.Center,
            ) {
                leadingIcon()
            }

            Column {
                Text(
                    text = caption,
                    style = VoltType.labelSmall.copy(
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp,
                    ),
                    color = VoltColors.onSurfaceMuted,
                )
                Spacer(Modifier.height(2.dp))
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .clickable(onClick = onValueTap)
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.Bottom,
                ) {
                    Text(
                        text = value,
                        style = VoltType.displayMedium.copy(
                            fontSize = 36.sp,
                            fontWeight = FontWeight.ExtraBold,
                        ),
                        color = VoltColors.onSurface,
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = valueSuffix,
                        style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                        color = VoltColors.onSurfaceMuted,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
            }
        }

        // ── Right: stacked unit-pill toggle ────────────────────────────
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalAlignment = Alignment.End,
        ) {
            unitOptions.forEachIndexed { index, label ->
                UnitPill(
                    label = label,
                    selected = index == selectedUnitIndex,
                    onClick = { onUnitSelected(index) },
                )
            }
        }
    }
}

@Composable
private fun UnitPill(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) VoltColors.volt else VoltColors.surface2
    val fg = if (selected) VoltColors.onVolt else VoltColors.onSurfaceMuted
    Box(
        modifier = Modifier
            .defaultMinSize(minWidth = 56.dp)
            .height(32.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(bg)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = label,
            style = VoltType.labelLarge.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = fg,
        )
    }
}
