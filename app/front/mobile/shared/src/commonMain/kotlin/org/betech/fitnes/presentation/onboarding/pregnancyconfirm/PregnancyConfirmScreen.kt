package org.betech.fitnes.presentation.onboarding.pregnancyconfirm

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.DisclaimerKind
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltDisclaimer
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.trimesterpostpartum.TrimesterPostpartumScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 21 · Pregnancy Confirm (Pencil pqupj · "Hamiləlik Təsdiq").
 *
 * Static reassurance + safe-template branch entry. NOT an AI plan trigger;
 * confirms the user is entering the curated pregnancy/postpartum safe flow
 * (Trimester/Postpartum split next). Copy uses "həkim/mütəxəssis" wording
 * only — no "trainer / coach / professional" terminology (CLAUDE.md doctrine).
 *
 * Layout: dark surface0 with a soft volt glow at the top edge, 24dp horizontal
 * gutter, top bar (back chevron + centered "Təhlükəsiz plan"), eyebrow row
 * (volt heart-shield + caption), big title, subtitle, 4 surface1 list items
 * (icon tile + title/subtitle), INFO medical note, primary CTA, footnote.
 */
class PregnancyConfirmScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: PregnancyConfirmViewModel = koinViewModel()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                PregnancyConfirmSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.pop()
                }
                PregnancyConfirmSideEffect.NavigateToTrimester ->
                    navigator.push(TrimesterPostpartumScreen())
            }
        }

        PregnancyConfirmContent(
            strings = strings,
            onBack = { viewModel.onIntent(PregnancyConfirmIntent.BackTapped) },
            onConfirm = { viewModel.onIntent(PregnancyConfirmIntent.ConfirmTapped) },
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Content
// ──────────────────────────────────────────────────────────────────────

private data class SafeItem(
    val title: String,
    val subtitle: String,
    val draw: DrawScope.() -> Unit,
)

@Composable
private fun PregnancyConfirmContent(
    strings: Strings,
    onBack: () -> Unit,
    onConfirm: () -> Unit,
) {
    val items = listOf(
        SafeItem(
            title = strings.pregnancyConfirmItem1Title,
            subtitle = strings.pregnancyConfirmItem1Subtitle,
            draw = { drawMobilityArc() },
        ),
        SafeItem(
            title = strings.pregnancyConfirmItem2Title,
            subtitle = strings.pregnancyConfirmItem2Subtitle,
            draw = { drawBreathPosture() },
        ),
        SafeItem(
            title = strings.pregnancyConfirmItem3Title,
            subtitle = strings.pregnancyConfirmItem3Subtitle,
            draw = { drawPelvicAware() },
        ),
        SafeItem(
            title = strings.pregnancyConfirmItem4Title,
            subtitle = strings.pregnancyConfirmItem4Subtitle,
            draw = { drawStretch() },
        ),
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        // Soft volt glow at the top edge (subtle vertical gradient).
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(
                    Brush.verticalGradient(
                        0f to VoltColors.volt.copy(alpha = 0.08f),
                        1f to VoltColors.surface0.copy(alpha = 0f),
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
        ) {
            // ── Top bar: back chevron + centered title ────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                contentAlignment = Alignment.Center,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    BackChevronButton(onClick = onBack)
                }
                Text(
                    text = strings.pregnancyConfirmTopBarTitle,
                    style = VoltType.titleMedium,
                    color = VoltColors.onSurface,
                )
            }

            // ── Scrollable body via LazyColumn ────────────────────────
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                item {
                    Spacer(Modifier.height(16.dp))
                    EyebrowRow(text = strings.pregnancyConfirmEyebrow)
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = strings.pregnancyConfirmTitle,
                        style = VoltType.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                        ),
                        color = VoltColors.onSurface,
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = strings.pregnancyConfirmSubtitle,
                        style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                        color = VoltColors.onSurfaceMuted,
                    )
                    Spacer(Modifier.height(20.dp))
                }

                items(items) { item ->
                    SafeItemCard(item = item)
                    Spacer(Modifier.height(12.dp))
                }

                item {
                    Spacer(Modifier.height(8.dp))
                    VoltDisclaimer(
                        text = strings.pregnancyConfirmMedicalNote,
                        kind = DisclaimerKind.INFO,
                    )
                    Spacer(Modifier.height(20.dp))
                }
            }

            // ── CTA + footnote (bottom anchored) ──────────────────────
            VoltButton(
                text = strings.pregnancyConfirmCta,
                onClick = onConfirm,
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = strings.pregnancyConfirmFootnote,
                style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                color = VoltColors.onSurfaceMuted,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Eyebrow row — volt heart-shield glyph + caption
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun EyebrowRow(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Canvas(modifier = Modifier.size(16.dp)) { drawHeartShield() }
        Text(
            text = text,
            style = VoltType.labelLarge.copy(
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            ),
            color = VoltColors.volt,
        )
    }
}

private fun DrawScope.drawHeartShield() {
    val w = size.width
    val h = size.height
    val stroke = w / 9f
    val volt = VoltColors.volt

    // Shield silhouette.
    val shield = Path().apply {
        moveTo(w * 0.5f, h * 0.05f)
        lineTo(w * 0.90f, h * 0.22f)
        lineTo(w * 0.90f, h * 0.55f)
        quadraticBezierTo(w * 0.90f, h * 0.88f, w * 0.5f, h * 0.98f)
        quadraticBezierTo(w * 0.10f, h * 0.88f, w * 0.10f, h * 0.55f)
        lineTo(w * 0.10f, h * 0.22f)
        close()
    }
    drawPath(shield, color = volt, style = Stroke(width = stroke, cap = StrokeCap.Round))

    // Inner heart (two arcs + V).
    val heart = Path().apply {
        moveTo(w * 0.5f, h * 0.70f)
        cubicTo(
            w * 0.15f, h * 0.55f,
            w * 0.25f, h * 0.30f,
            w * 0.5f, h * 0.46f
        )
        cubicTo(
            w * 0.75f, h * 0.30f,
            w * 0.85f, h * 0.55f,
            w * 0.5f, h * 0.70f
        )
        close()
    }
    drawPath(heart, color = volt)
}

// ──────────────────────────────────────────────────────────────────────
// Safe-item card — surface1 rounded row: icon tile + title + subtitle
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun SafeItemCard(item: SafeItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(VoltColors.volt.copy(alpha = 0.12f))
                .border(1.dp, VoltColors.volt.copy(alpha = 0.35f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Canvas(modifier = Modifier.size(22.dp)) { item.draw(this) }
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = item.title,
                style = VoltType.titleMedium.copy(fontSize = 15.sp, fontWeight = FontWeight.SemiBold),
                color = VoltColors.onSurface,
            )
            Text(
                text = item.subtitle,
                style = VoltType.bodyMedium.copy(fontSize = 13.sp),
                color = VoltColors.onSurfaceMuted,
            )
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Pure-Canvas glyphs for the 4 safe-item icons (no asset deps)
// ──────────────────────────────────────────────────────────────────────

private fun DrawScope.drawMobilityArc() {
    val volt = VoltColors.volt
    val sw = size.minDimension / 9f
    // Two opposing arcs evoking range-of-motion.
    drawArc(
        color = volt,
        startAngle = 200f, sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(size.width * 0.10f, size.height * 0.10f),
        size = androidx.compose.ui.geometry.Size(size.width * 0.80f, size.height * 0.80f),
        style = Stroke(width = sw, cap = StrokeCap.Round),
    )
    drawCircle(color = volt, radius = sw, center = Offset(size.width * 0.50f, size.height * 0.50f))
}

private fun DrawScope.drawBreathPosture() {
    val volt = VoltColors.volt
    val sw = size.minDimension / 9f
    // Vertical spine line + 3 horizontal breath ripples.
    drawLine(
        color = volt,
        start = Offset(size.width * 0.5f, size.height * 0.10f),
        end = Offset(size.width * 0.5f, size.height * 0.90f),
        strokeWidth = sw, cap = StrokeCap.Round,
    )
    listOf(0.25f, 0.50f, 0.75f).forEach { y ->
        drawLine(
            color = volt,
            start = Offset(size.width * 0.15f, size.height * y),
            end = Offset(size.width * 0.85f, size.height * y),
            strokeWidth = sw * 0.7f, cap = StrokeCap.Round,
        )
    }
}

private fun DrawScope.drawPelvicAware() {
    val volt = VoltColors.volt
    val sw = size.minDimension / 9f
    // Pelvic bowl: lower arc + small inner dot.
    drawArc(
        color = volt,
        startAngle = 20f, sweepAngle = 140f,
        useCenter = false,
        topLeft = Offset(size.width * 0.05f, size.height * 0.05f),
        size = androidx.compose.ui.geometry.Size(size.width * 0.90f, size.height * 0.90f),
        style = Stroke(width = sw, cap = StrokeCap.Round),
    )
    drawCircle(color = volt, radius = sw * 0.9f, center = Offset(size.width * 0.5f, size.height * 0.55f))
}

private fun DrawScope.drawStretch() {
    val volt = VoltColors.volt
    val sw = size.minDimension / 9f
    // Two opposing arrows / extension lines.
    drawLine(
        color = volt,
        start = Offset(size.width * 0.15f, size.height * 0.85f),
        end = Offset(size.width * 0.85f, size.height * 0.15f),
        strokeWidth = sw, cap = StrokeCap.Round,
    )
    // Top-right arrow head.
    drawLine(
        color = volt,
        start = Offset(size.width * 0.85f, size.height * 0.15f),
        end = Offset(size.width * 0.55f, size.height * 0.15f),
        strokeWidth = sw, cap = StrokeCap.Round,
    )
    drawLine(
        color = volt,
        start = Offset(size.width * 0.85f, size.height * 0.15f),
        end = Offset(size.width * 0.85f, size.height * 0.45f),
        strokeWidth = sw, cap = StrokeCap.Round,
    )
    // Bottom-left arrow head.
    drawLine(
        color = volt,
        start = Offset(size.width * 0.15f, size.height * 0.85f),
        end = Offset(size.width * 0.45f, size.height * 0.85f),
        strokeWidth = sw, cap = StrokeCap.Round,
    )
    drawLine(
        color = volt,
        start = Offset(size.width * 0.15f, size.height * 0.85f),
        end = Offset(size.width * 0.15f, size.height * 0.55f),
        strokeWidth = sw, cap = StrokeCap.Round,
    )
}

// ──────────────────────────────────────────────────────────────────────
// Back chevron — shared atom (mirrors PregnancyNudge / DeleteAcc1)
// ──────────────────────────────────────────────────────────────────────

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
        Canvas(modifier = Modifier.size(14.dp)) { drawBackChevron() }
    }
}

private fun DrawScope.drawBackChevron() {
    val strokeWidth = size.minDimension / 8f
    val midY = size.height / 2f
    val left = size.width * 0.25f
    val right = size.width * 0.75f
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(right, 0f),
        end = Offset(left, midY),
        strokeWidth = strokeWidth, cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(left, midY),
        end = Offset(right, size.height),
        strokeWidth = strokeWidth, cap = StrokeCap.Round,
    )
}
