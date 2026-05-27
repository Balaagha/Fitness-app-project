package org.betech.fitnes.presentation.onboarding.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Canvas
import androidx.compose.ui.text.font.FontWeight
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.typography.VoltType

/**
 * Reusable scaffold for the 7 mandatory onboarding question screens (Q1–Q7).
 *
 * Layout (top → bottom):
 *   • Top bar — circular back button (left) + 7-segment [QuestionProgressBar] (right)
 *   • Header — title + optional subtitle
 *   • Content — caller-provided question body (Column scope)
 *   • Bottom CTA — caller-provided primary button (typically `VoltButton`)
 *
 * Side padding is 24dp throughout. Background is `surface0` (per Pencil S5QT23).
 *
 * @param progress current step / total (1..7) used by [QuestionProgressBar].
 * @param title the large primary question text.
 * @param subtitle optional muted supporting line under the title.
 * @param onBack invoked when the back chevron is tapped.
 * @param primaryCta bottom-anchored full-width CTA composable (caller-owned).
 * @param modifier outermost modifier — applied to the root [Box]. Default is empty.
 * @param content the question-body content lambda; receives [ColumnScope].
 */
@Composable
fun QuestionScaffold(
    progress: QuestionProgress,
    title: String,
    subtitle: String?,
    onBack: () -> Unit,
    primaryCta: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
        ) {
            // ── Top bar ────────────────────────────────────────────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                BackChevronButton(onClick = onBack)
                QuestionProgressBar(progress = progress, modifier = Modifier.weight(1f))
            }

            Spacer(Modifier.height(24.dp))

            // ── Header ─────────────────────────────────────────────────
            Text(
                text = title,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
            )
            if (!subtitle.isNullOrEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = subtitle,
                    style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                    color = VoltColors.onSurfaceMuted,
                )
            }

            Spacer(Modifier.height(24.dp))

            // ── Question content ───────────────────────────────────────
            content()

            Spacer(Modifier.weight(1f))

            // ── Bottom CTA ─────────────────────────────────────────────
            primaryCta()

            Spacer(Modifier.height(24.dp))
        }
    }
}

/** 36dp circular back button with a vector-drawn `<` chevron — zero-dep. */
@Composable
private fun BackChevronButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(14.dp)) {
            drawBackChevron()
        }
    }
}

private fun DrawScope.drawBackChevron() {
    val strokeWidth = size.minDimension / 8f
    val midY = size.height / 2f
    val left = size.width * 0.25f
    val right = size.width * 0.75f
    // Top diagonal of "<"
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(right, 0f),
        end = Offset(left, midY),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
    // Bottom diagonal of "<"
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(left, midY),
        end = Offset(right, size.height),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}
