package org.betech.fitnes.presentation.onboarding.q7daysession

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import kotlinx.collections.immutable.toImmutableList
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltSegmentedRow
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.common.QuestionProgress
import org.betech.fitnes.presentation.onboarding.common.QuestionScaffold
import org.betech.fitnes.presentation.onboarding.profilesummary.ProfileSummaryScreen
import org.betech.fitnes.presentation.onboarding.q6context.Q6ContextScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 1.7 · Q7 Day + Session (Pencil H0uZ0e · "Q7 · Günlər + Sessiya").
 *
 * Final step of the 7 required onboarding questions. Two segmented rows
 * (days/week, session minutes) + a dynamic volt-tinted summary chip + a
 * "Bitir" CTA that emits `OnboardingCompleted` and routes to ProfileSummary.
 *
 * Reuses [QuestionScaffold] with the same inline eyebrow pattern Q5/Q6
 * established. The segmented row is extracted as [VoltSegmentedRow] —
 * the chip pattern is identical to future schedule / preset pickers.
 */
class Q7DaySessionScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q7DaySessionViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q7DaySessionSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(Q6ContextScreen())
                }
                Q7DaySessionSideEffect.NavigateToProfileSummary ->
                    navigator.push(ProfileSummaryScreen())
                is Q7DaySessionSideEffect.ShowError -> Unit // toast hook later
            }
        }

        Q7DaySessionContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(Q7DaySessionIntent.BackTapped) },
            onDaysSelected = { viewModel.onIntent(Q7DaySessionIntent.SetDays(it)) },
            onMinutesSelected = { viewModel.onIntent(Q7DaySessionIntent.SetMinutes(it)) },
            onConfirm = { viewModel.onIntent(Q7DaySessionIntent.Confirm) },
        )
    }
}

@Composable
private fun Q7DaySessionContent(
    state: Q7DaySessionState,
    strings: Strings,
    onBack: () -> Unit,
    onDaysSelected: (Int) -> Unit,
    onMinutesSelected: (Int) -> Unit,
    onConfirm: () -> Unit,
) {
    QuestionScaffold(
        progress = QuestionProgress(current = 7, total = 7),
        title = strings.q7Title,
        subtitle = strings.q7Subtitle,
        onBack = onBack,
        primaryCta = {
            VoltButton(
                text = strings.commonFinish,
                onClick = onConfirm,
                enabled = !state.isSaving,
            )
        },
    ) {
        // Eyebrow (Q5/Q6 inline pattern).
        Text(
            text = strings.q7Eyebrow,
            style = VoltType.labelLarge.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            ),
            color = VoltColors.onSurfaceMuted,
        )
        Spacer(Modifier.height(24.dp))

        // ── Section 1 · Weekly days ────────────────────────────────
        SectionCaption(text = strings.q7DaysCaption)
        Spacer(Modifier.height(8.dp))
        VoltSegmentedRow(
            options = Q7DaysOptions.toImmutableList(),
            selected = state.daysPerWeek,
            onSelected = onDaysSelected,
            label = { it.toString() },
        )

        Spacer(Modifier.height(24.dp))

        // ── Section 2 · Session minutes ────────────────────────────
        SectionCaption(text = strings.q7SessionCaption)
        Spacer(Modifier.height(8.dp))
        VoltSegmentedRow(
            options = Q7MinutesOptions.toImmutableList(),
            selected = state.sessionMinutes,
            onSelected = onMinutesSelected,
            label = { it.toString() },
        )

        Spacer(Modifier.height(16.dp))

        // ── Summary chip (volt-tinted info banner) ─────────────────
        SummaryChip(
            text = strings.q7Summary(state.daysPerWeek, state.sessionMinutes),
        )
    }
}

@Composable
private fun SectionCaption(text: String) {
    Text(
        text = text,
        style = VoltType.labelLarge.copy(
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 2.sp,
        ),
        color = VoltColors.onSurfaceMuted,
    )
}

/**
 * Volt-tinted summary banner — `volt @ 10% alpha` fill + 1dp volt outline
 * + `onSurface` body text. Small calendar glyph leads.
 */
@Composable
private fun SummaryChip(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = VoltColors.volt.copy(alpha = 0.10f),
                shape = RoundedCornerShape(12.dp),
            )
            .border(
                width = 1.dp,
                color = VoltColors.volt,
                shape = RoundedCornerShape(12.dp),
            )
            .padding(horizontal = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        CalendarGlyph()
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
            ),
            color = VoltColors.onSurface,
        )
    }
}

/** Tiny vector-drawn calendar glyph — zero-dep, matches design accent. */
@Composable
private fun CalendarGlyph() {
    Box(modifier = Modifier.size(16.dp), contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.size(16.dp)) {
            val w = size.width
            val h = size.height
            val sw = w / 12f
            val c = VoltColors.volt
            // Outer rectangle (the calendar body, slightly inset from top).
            drawRoundRectOutline(
                left = w * 0.10f,
                top = h * 0.22f,
                right = w * 0.90f,
                bottom = h * 0.90f,
                stroke = sw,
                color = c,
            )
            // Top binding bar
            drawLine(
                color = c,
                start = Offset(w * 0.10f, h * 0.38f),
                end = Offset(w * 0.90f, h * 0.38f),
                strokeWidth = sw,
                cap = StrokeCap.Round,
            )
            // Two pins
            drawLine(
                color = c,
                start = Offset(w * 0.32f, h * 0.10f),
                end = Offset(w * 0.32f, h * 0.30f),
                strokeWidth = sw,
                cap = StrokeCap.Round,
            )
            drawLine(
                color = c,
                start = Offset(w * 0.68f, h * 0.10f),
                end = Offset(w * 0.68f, h * 0.30f),
                strokeWidth = sw,
                cap = StrokeCap.Round,
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawRoundRectOutline(
    left: Float,
    top: Float,
    right: Float,
    bottom: Float,
    stroke: Float,
    color: androidx.compose.ui.graphics.Color,
) {
    val path = androidx.compose.ui.graphics.Path().apply {
        moveTo(left, top)
        lineTo(right, top)
        lineTo(right, bottom)
        lineTo(left, bottom)
        close()
    }
    drawPath(
        path = path,
        color = color,
        style = Stroke(width = stroke, cap = StrokeCap.Round, join = StrokeJoin.Round),
    )
}
