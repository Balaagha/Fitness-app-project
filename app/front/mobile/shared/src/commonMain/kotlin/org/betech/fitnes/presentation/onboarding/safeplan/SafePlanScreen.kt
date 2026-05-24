package org.betech.fitnes.presentation.onboarding.safeplan

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
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
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltButtonGhost
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.todaysafeworkout.TodaySafeWorkoutScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 23 · Safe 4-week Plan (Pencil o0BUd · "Sənin 4 həftəlik planın").
 *
 * Entry from TrimesterPostpartum → presents a curated static 4-week safe
 * template. The screen renders:
 *   - Top bar: back chevron + small gear/filter icon (right)
 *   - Eyebrow (volt small caps) + big title + muted subtitle
 *   - 3 equal-width metric chips (weeks · sessions · duration)
 *   - "Bu həftə" section header + week indicator
 *   - 7-day pill row with today (Çərşənbə Axşamı / index=1) highlighted volt
 *   - Volt-tinted focus banner (postural · breath · mobility · 5 kg cap)
 *   - Primary CTA "Bu günkü məşqi aç" → TodaySafeWorkoutScreen
 *   - Ghost CTA "Plan haqqında məlumat"
 *
 * Invariant (CLAUDE.md): the safe plan is a curated static template; this
 * screen NEVER triggers AI plan generation (pregnancy_postpartum=true is a
 * hard-stop on AI plan flows).
 */
class SafePlanScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: SafePlanViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                SafePlanSideEffect.NavigateBack -> {
                    navigator.pop()
                }
                SafePlanSideEffect.NavigateToTodayWorkout ->
                    navigator.push(TodaySafeWorkoutScreen())
                SafePlanSideEffect.NavigateToPlanInfo -> Unit // info screen lands next iter
            }
        }

        SafePlanContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(SafePlanIntent.BackTapped) },
            onSelectDay = { viewModel.onIntent(SafePlanIntent.SelectDay(it)) },
            onOpenToday = { viewModel.onIntent(SafePlanIntent.OpenTodayTapped) },
            onOpenInfo = { viewModel.onIntent(SafePlanIntent.OpenInfoTapped) },
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Content
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun SafePlanContent(
    state: SafePlanState,
    strings: Strings,
    onBack: () -> Unit,
    onSelectDay: (Int) -> Unit,
    onOpenToday: () -> Unit,
    onOpenInfo: () -> Unit,
) {
    val dayLabels = listOf(
        strings.safePlanDayMon,
        strings.safePlanDayTue,
        strings.safePlanDayWed,
        strings.safePlanDayThu,
        strings.safePlanDayFri,
        strings.safePlanDaySat,
        strings.safePlanDaySun,
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        // Subtle volt glow at the very top edge.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(
                    Brush.verticalGradient(
                        0f to VoltColors.volt.copy(alpha = 0.06f),
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
            // ── Top bar: back chevron (left) + gear (right) ───────────
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                BackChevronButton(onClick = onBack)
                GearButton()
            }

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                item {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = strings.safePlanEyebrow,
                        style = VoltType.labelLarge.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp,
                        ),
                        color = VoltColors.volt,
                    )
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = strings.safePlanTitle,
                        style = VoltType.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp,
                        ),
                        color = VoltColors.onSurface,
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = strings.safePlanSubtitle,
                        style = VoltType.bodyMedium.copy(fontSize = 14.sp),
                        color = VoltColors.onSurfaceMuted,
                    )
                    Spacer(Modifier.height(20.dp))
                }

                item {
                    MetricChipsRow(strings = strings)
                    Spacer(Modifier.height(24.dp))
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = strings.safePlanSectionThisWeek,
                            style = VoltType.titleMedium.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            ),
                            color = VoltColors.onSurface,
                        )
                        Text(
                            text = strings.safePlanWeekIndicator,
                            style = VoltType.bodyMedium.copy(fontSize = 12.sp),
                            color = VoltColors.onSurfaceMuted,
                        )
                    }
                    Spacer(Modifier.height(12.dp))
                    DayPillRow(
                        labels = dayLabels,
                        dose = strings.safePlanDayDose,
                        selectedIndex = state.selectedDayIndex,
                        onSelect = onSelectDay,
                    )
                    Spacer(Modifier.height(20.dp))
                }

                item {
                    FocusBanner(text = strings.safePlanFocusBanner)
                    Spacer(Modifier.height(20.dp))
                }
            }

            // ── CTA group ────────────────────────────────────────────
            VoltButton(
                text = strings.safePlanCtaToday,
                onClick = onOpenToday,
            )
            Spacer(Modifier.height(4.dp))
            VoltButtonGhost(
                text = strings.safePlanCtaInfo,
                onClick = onOpenInfo,
            )
            Spacer(Modifier.height(16.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────
// Metric chips — 3 equal-width surface1 cards
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun MetricChipsRow(strings: Strings) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        MetricChip(
            modifier = Modifier.weight(1f),
            value = strings.safePlanMetric1Value,
            label = strings.safePlanMetric1Label,
        )
        MetricChip(
            modifier = Modifier.weight(1f),
            value = strings.safePlanMetric2Value,
            label = strings.safePlanMetric2Label,
        )
        MetricChip(
            modifier = Modifier.weight(1f),
            value = strings.safePlanMetric3Value,
            label = strings.safePlanMetric3Label,
        )
    }
}

@Composable
private fun MetricChip(
    modifier: Modifier = Modifier,
    value: String,
    label: String,
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = value,
            style = VoltType.titleMedium.copy(
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = VoltColors.onSurface,
        )
        Text(
            text = label,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp),
            color = VoltColors.onSurfaceMuted,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// 7-day pill row — initials + dose; selected day = volt
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun DayPillRow(
    labels: List<String>,
    dose: String,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        labels.forEachIndexed { index, label ->
            DayPill(
                modifier = Modifier.weight(1f),
                label = label,
                dose = dose,
                selected = index == selectedIndex,
                onClick = { onSelect(index) },
            )
        }
    }
}

@Composable
private fun DayPill(
    modifier: Modifier = Modifier,
    label: String,
    dose: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bg = if (selected) VoltColors.volt else VoltColors.surface1
    val labelColor = if (selected) VoltColors.onVolt else VoltColors.onSurface
    val doseColor = if (selected) VoltColors.onVolt.copy(alpha = 0.75f) else VoltColors.onSurfaceMuted
    val border = if (selected) VoltColors.volt else VoltColors.outline

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(bg)
            .border(1.dp, border, RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Text(
            text = label,
            style = VoltType.titleMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
            ),
            color = labelColor,
        )
        Text(
            text = dose,
            style = VoltType.bodyMedium.copy(fontSize = 9.sp),
            color = doseColor,
            textAlign = TextAlign.Center,
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Focus banner — volt-tinted info card
// ──────────────────────────────────────────────────────────────────────

@Composable
private fun FocusBanner(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(VoltColors.volt.copy(alpha = 0.08f))
            .border(1.dp, VoltColors.volt.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
            .padding(horizontal = 14.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .size(20.dp)
                .clip(CircleShape)
                .background(VoltColors.volt),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "i",
                style = VoltType.labelLarge.copy(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = VoltColors.onVolt,
            )
        }
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 13.sp),
            color = VoltColors.onSurface,
            modifier = Modifier.weight(1f),
        )
    }
}

// ──────────────────────────────────────────────────────────────────────
// Top-bar atoms — back chevron + gear glyph
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

@Composable
private fun GearButton() {
    // Decorative-only for now (no settings target this iteration).
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(16.dp)) { drawGear() }
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

private fun DrawScope.drawGear() {
    val c = VoltColors.onSurface
    val sw = size.minDimension / 10f
    val cx = size.width / 2f
    val cy = size.height / 2f
    val r = size.minDimension * 0.30f
    // Center ring
    drawCircle(color = c, radius = r, center = Offset(cx, cy), style = Stroke(width = sw, cap = StrokeCap.Round))
    // Inner dot
    drawCircle(color = c, radius = sw * 0.9f, center = Offset(cx, cy))
    // Six teeth (radial ticks).
    val teeth = 6
    val outer = size.minDimension * 0.48f
    val inner = r + sw
    for (i in 0 until teeth) {
        val angle = (i * 60.0) * (kotlin.math.PI / 180.0)
        val cosA = kotlin.math.cos(angle).toFloat()
        val sinA = kotlin.math.sin(angle).toFloat()
        drawLine(
            color = c,
            start = Offset(cx + cosA * inner, cy + sinA * inner),
            end = Offset(cx + cosA * outer, cy + sinA * outer),
            strokeWidth = sw, cap = StrokeCap.Round,
        )
    }
}
