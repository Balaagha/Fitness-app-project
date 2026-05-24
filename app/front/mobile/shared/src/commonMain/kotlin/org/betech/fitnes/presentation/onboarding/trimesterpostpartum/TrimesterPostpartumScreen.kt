package org.betech.fitnes.presentation.onboarding.trimesterpostpartum

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
import org.betech.fitnes.designsystem.components.VoltOptionCard
import org.betech.fitnes.designsystem.components.VoltOptionIconTile
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.common.QuestionProgress
import org.betech.fitnes.presentation.onboarding.common.QuestionScaffold
import org.betech.fitnes.presentation.onboarding.pregnancyconfirm.PregnancyConfirmScreen
import org.betech.fitnes.presentation.onboarding.safeplan.SafePlanScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 22 · Trimester / Postpartum (Pencil C6Ya4A · "Hansı dövrdəsən?").
 *
 * 4-option single-choice picker that branches the curated safe-template
 * pipeline. Reached from PregnancyConfirm; routes forward to SafePlan stub.
 * Default selection = T2 (most common; safest tap-through).
 *
 * Layout reuses [QuestionScaffold] with a 3-step progress bar (1/3 filled);
 * the eyebrow label "1 / 3 · DÖVR" is rendered inline at the top of the
 * content slot to mirror the Q5–Q7 pattern without modifying the scaffold.
 *
 * Invariant (CLAUDE.md): NOT an AI plan trigger. pregnancy_postpartum=true
 * forces curated static templates only.
 */
class TrimesterPostpartumScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: TrimesterPostpartumViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                TrimesterPostpartumSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(PregnancyConfirmScreen())
                }
                TrimesterPostpartumSideEffect.NavigateToSafePlan ->
                    navigator.push(SafePlanScreen())
                is TrimesterPostpartumSideEffect.ShowError -> Unit // toast hook later
            }
        }

        TrimesterPostpartumContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(TrimesterPostpartumIntent.BackTapped) },
            onSelect = { viewModel.onIntent(TrimesterPostpartumIntent.Select(it)) },
            onConfirm = { viewModel.onIntent(TrimesterPostpartumIntent.Confirm) },
        )
    }
}

@Composable
private fun TrimesterPostpartumContent(
    state: TrimesterPostpartumState,
    strings: Strings,
    onBack: () -> Unit,
    onSelect: (TrimesterChoice) -> Unit,
    onConfirm: () -> Unit,
) {
    QuestionScaffold(
        progress = QuestionProgress(current = 1, total = 3),
        title = strings.trimesterTitle,
        subtitle = strings.trimesterSubtitle,
        onBack = onBack,
        primaryCta = {
            VoltButton(
                text = strings.continueAction,
                onClick = onConfirm,
                enabled = !state.isSaving,
            )
        },
    ) {
        // Inline eyebrow (above the 4 cards) — mirrors Q5–Q7 pattern.
        Text(
            text = strings.trimesterEyebrow,
            style = VoltType.labelLarge.copy(
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
            ),
            color = VoltColors.onSurfaceMuted,
        )
        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TrimesterRow(
                choice = TrimesterChoice.T1,
                title = strings.trimester1Title,
                subtitle = strings.trimester1Subtitle,
                selected = state.selected == TrimesterChoice.T1,
                onClick = onSelect,
                glyph = { sel -> ClockGlyph(selected = sel) },
            )
            TrimesterRow(
                choice = TrimesterChoice.T2,
                title = strings.trimester2Title,
                subtitle = strings.trimester2Subtitle,
                selected = state.selected == TrimesterChoice.T2,
                onClick = onSelect,
                glyph = { sel -> ClockGlyph(selected = sel) },
            )
            TrimesterRow(
                choice = TrimesterChoice.T3,
                title = strings.trimester3Title,
                subtitle = strings.trimester3Subtitle,
                selected = state.selected == TrimesterChoice.T3,
                onClick = onSelect,
                glyph = { sel -> ClockGlyph(selected = sel) },
            )
            TrimesterRow(
                choice = TrimesterChoice.POSTPARTUM,
                title = strings.trimesterPostpartumTitle,
                subtitle = strings.trimesterPostpartumSubtitle,
                selected = state.selected == TrimesterChoice.POSTPARTUM,
                onClick = onSelect,
                glyph = { sel -> SparkleGlyph(selected = sel) },
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = strings.trimesterFootnote,
            style = VoltType.bodyMedium.copy(fontSize = 12.sp),
            color = VoltColors.onSurfaceMuted,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun TrimesterRow(
    choice: TrimesterChoice,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: (TrimesterChoice) -> Unit,
    glyph: @Composable (selected: Boolean) -> Unit,
) {
    VoltOptionCard(
        title = title,
        subtitle = subtitle,
        selected = selected,
        onClick = { onClick(choice) },
        leadingIcon = {
            VoltOptionIconTile(selected = selected) {
                glyph(selected)
            }
        },
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Zero-dep glyphs — clock for staged trimesters, sparkle for postpartum.
// ─────────────────────────────────────────────────────────────────────────────

private fun glyphColor(selected: Boolean): Color =
    if (selected) VoltColors.onVolt else VoltColors.onSurface

@Composable
private fun ClockGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 11f
        // Outer ring
        drawArc(
            color = c,
            startAngle = 0f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = Offset(sw / 2f, sw / 2f),
            size = Size(w - sw, h - sw),
            style = Stroke(width = sw, cap = StrokeCap.Round),
        )
        // Hour hand (up)
        drawLine(
            color = c,
            start = Offset(w / 2f, h / 2f),
            end = Offset(w / 2f, h * 0.28f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Minute hand (right)
        drawLine(
            color = c,
            start = Offset(w / 2f, h / 2f),
            end = Offset(w * 0.74f, h / 2f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun SparkleGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 8f
        // Vertical
        drawLine(
            color = c,
            start = Offset(w / 2f, h * 0.10f),
            end = Offset(w / 2f, h * 0.90f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Horizontal
        drawLine(
            color = c,
            start = Offset(w * 0.10f, h / 2f),
            end = Offset(w * 0.90f, h / 2f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Diagonal 1
        drawLine(
            color = c,
            start = Offset(w * 0.22f, h * 0.22f),
            end = Offset(w * 0.78f, h * 0.78f),
            strokeWidth = sw * 0.7f,
            cap = StrokeCap.Round,
        )
        // Diagonal 2
        drawLine(
            color = c,
            start = Offset(w * 0.78f, h * 0.22f),
            end = Offset(w * 0.22f, h * 0.78f),
            strokeWidth = sw * 0.7f,
            cap = StrokeCap.Round,
        )
    }
}
