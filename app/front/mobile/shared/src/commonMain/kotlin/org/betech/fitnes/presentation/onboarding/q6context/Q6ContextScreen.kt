package org.betech.fitnes.presentation.onboarding.q6context

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
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
import org.betech.fitnes.presentation.onboarding.q5experience.Q5ExperienceScreen
import org.betech.fitnes.presentation.onboarding.q7daysession.Q7DaySessionScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 1.6 · Q6 Training Context (Pencil F16e8 · "Q6 · Kontekst").
 *
 * Step 6 of 7. Single-choice over 4 location/equipment buckets that map
 * down to the 3-valued [org.betech.fitnes.domain.model.TrainingContext]
 * schema column. Default = HOME_BODYWEIGHT (safest tap-through).
 *
 * Uses the inline eyebrow pattern Q5 introduced — no scaffold change.
 */
class Q6ContextScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q6ContextViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q6ContextSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(Q5ExperienceScreen())
                }
                Q6ContextSideEffect.NavigateToQ7DaySession ->
                    navigator.push(Q7DaySessionScreen())
                is Q6ContextSideEffect.ShowError -> Unit // toast hook later
            }
        }

        Q6ContextContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(Q6ContextIntent.BackTapped) },
            onSelect = { viewModel.onIntent(Q6ContextIntent.Select(it)) },
            onConfirm = { viewModel.onIntent(Q6ContextIntent.Confirm) },
        )
    }
}

@Composable
private fun Q6ContextContent(
    state: Q6ContextState,
    strings: Strings,
    onBack: () -> Unit,
    onSelect: (ContextChoice) -> Unit,
    onConfirm: () -> Unit,
) {
    QuestionScaffold(
        progress = QuestionProgress(current = 6, total = 7),
        title = strings.q6Title,
        subtitle = strings.q6Subtitle,
        onBack = onBack,
        primaryCta = {
            VoltButton(
                text = strings.continueAction,
                onClick = onConfirm,
                enabled = state.selected != null && !state.isSaving,
            )
        },
    ) {
        // Eyebrow — inlined at top of content slot (Q5 pattern).
        Text(
            text = strings.q6Eyebrow,
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
            ContextRow(
                choice = ContextChoice.HOME_BODYWEIGHT,
                title = strings.q6OptionHomeBodyweightTitle,
                subtitle = strings.q6OptionHomeBodyweightSubtitle,
                selected = state.selected == ContextChoice.HOME_BODYWEIGHT,
                onClick = onSelect,
                glyph = { sel -> HouseGlyph(selected = sel) },
            )
            ContextRow(
                choice = ContextChoice.HOME_EQUIPMENT,
                title = strings.q6OptionHomeEquipmentTitle,
                subtitle = strings.q6OptionHomeEquipmentSubtitle,
                selected = state.selected == ContextChoice.HOME_EQUIPMENT,
                onClick = onSelect,
                glyph = { sel -> HouseDumbbellGlyph(selected = sel) },
            )
            ContextRow(
                choice = ContextChoice.GYM,
                title = strings.q6OptionGymTitle,
                subtitle = strings.q6OptionGymSubtitle,
                selected = state.selected == ContextChoice.GYM,
                onClick = onSelect,
                glyph = { sel -> BuildingGlyph(selected = sel) },
            )
            ContextRow(
                choice = ContextChoice.HYBRID,
                title = strings.q6OptionHybridTitle,
                subtitle = strings.q6OptionHybridSubtitle,
                selected = state.selected == ContextChoice.HYBRID,
                onClick = onSelect,
                glyph = { sel -> CrossedArrowsGlyph(selected = sel) },
            )
            Spacer(Modifier.height(0.dp))
        }
    }
}

@Composable
private fun ContextRow(
    choice: ContextChoice,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: (ContextChoice) -> Unit,
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
// Zero-dep glyphs — geometric outlines per design note.
// ─────────────────────────────────────────────────────────────────────────────

private fun glyphColor(selected: Boolean): Color =
    if (selected) VoltColors.onVolt else VoltColors.onSurface

/** House outline — pitched roof + square base. */
@Composable
private fun HouseGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 10f
        val house = Path().apply {
            moveTo(w * 0.15f, h * 0.92f)
            lineTo(w * 0.15f, h * 0.48f)
            lineTo(w * 0.50f, h * 0.16f)
            lineTo(w * 0.85f, h * 0.48f)
            lineTo(w * 0.85f, h * 0.92f)
            close()
        }
        drawPath(
            path = house,
            color = c,
            style = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
    }
}

/** House + small dumbbell beside it — house outline + tiny bar/caps. */
@Composable
private fun HouseDumbbellGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(24.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 12f
        // House — left-shifted, slightly smaller
        val house = Path().apply {
            moveTo(w * 0.08f, h * 0.90f)
            lineTo(w * 0.08f, h * 0.50f)
            lineTo(w * 0.36f, h * 0.22f)
            lineTo(w * 0.64f, h * 0.50f)
            lineTo(w * 0.64f, h * 0.90f)
            close()
        }
        drawPath(
            path = house,
            color = c,
            style = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
        // Small dumbbell — bar
        drawLine(
            color = c,
            start = Offset(w * 0.74f, h * 0.70f),
            end = Offset(w * 0.92f, h * 0.70f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Left cap
        drawLine(
            color = c,
            start = Offset(w * 0.73f, h * 0.60f),
            end = Offset(w * 0.73f, h * 0.80f),
            strokeWidth = sw * 1.8f,
            cap = StrokeCap.Round,
        )
        // Right cap
        drawLine(
            color = c,
            start = Offset(w * 0.93f, h * 0.60f),
            end = Offset(w * 0.93f, h * 0.80f),
            strokeWidth = sw * 1.8f,
            cap = StrokeCap.Round,
        )
    }
}

/** Building — tall rectangle with two stacked rows of windows. */
@Composable
private fun BuildingGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 11f
        // Outer rectangle
        val rect = Path().apply {
            moveTo(w * 0.22f, h * 0.12f)
            lineTo(w * 0.78f, h * 0.12f)
            lineTo(w * 0.78f, h * 0.92f)
            lineTo(w * 0.22f, h * 0.92f)
            close()
        }
        drawPath(
            path = rect,
            color = c,
            style = Stroke(width = sw, cap = StrokeCap.Round, join = StrokeJoin.Round),
        )
        // Windows — 3 rows × 2 columns, small filled dots
        val dotR = sw * 0.75f
        val cols = listOf(0.38f, 0.62f)
        val rows = listOf(0.32f, 0.52f, 0.72f)
        for (ry in rows) {
            for (cx in cols) {
                drawCircle(color = c, radius = dotR, center = Offset(w * cx, h * ry))
            }
        }
    }
}

/** Crossed arrows — two diagonal lines with arrowheads pointing opposite. */
@Composable
private fun CrossedArrowsGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 10f
        // Diagonal 1 (top-left → bottom-right)
        drawLine(
            color = c,
            start = Offset(w * 0.18f, h * 0.18f),
            end = Offset(w * 0.82f, h * 0.82f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Arrowhead bottom-right
        drawLine(
            color = c,
            start = Offset(w * 0.82f, h * 0.82f),
            end = Offset(w * 0.62f, h * 0.82f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = c,
            start = Offset(w * 0.82f, h * 0.82f),
            end = Offset(w * 0.82f, h * 0.62f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Diagonal 2 (top-right → bottom-left)
        drawLine(
            color = c,
            start = Offset(w * 0.82f, h * 0.18f),
            end = Offset(w * 0.18f, h * 0.82f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Arrowhead top-right
        drawLine(
            color = c,
            start = Offset(w * 0.82f, h * 0.18f),
            end = Offset(w * 0.62f, h * 0.18f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = c,
            start = Offset(w * 0.82f, h * 0.18f),
            end = Offset(w * 0.82f, h * 0.38f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}
