package org.betech.fitnes.presentation.onboarding.q2sex

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltOptionCard
import org.betech.fitnes.designsystem.components.VoltOptionIconTile
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.common.QuestionProgress
import org.betech.fitnes.presentation.onboarding.common.QuestionScaffold
import org.betech.fitnes.presentation.onboarding.q1goal.Q1GoalScreen
import org.betech.fitnes.presentation.onboarding.q3age.Q3AgeScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 1.2 · Q2 Sex (Pencil ObxuP · "Q2 · Cinsiyyət").
 *
 * Step 2 of 7 mandatory onboarding questions. 3 UI options
 * ([SexChoice]) mapping to the binary domain enum
 * [org.betech.fitnes.domain.model.Sex] — PREFER_NOT_TO_SAY → null
 * (see [Q2SexViewModel]).
 */
class Q2SexScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q2SexViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q2SexSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(Q1GoalScreen())
                }
                Q2SexSideEffect.NavigateToQ3Age -> navigator.push(Q3AgeScreen())
                is Q2SexSideEffect.ShowError -> Unit // toast hook later
            }
        }

        Q2SexContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(Q2SexIntent.BackTapped) },
            onSelect = { viewModel.onIntent(Q2SexIntent.Select(it)) },
            onConfirm = { viewModel.onIntent(Q2SexIntent.Confirm) },
        )
    }
}

@Composable
private fun Q2SexContent(
    state: Q2SexState,
    strings: Strings,
    onBack: () -> Unit,
    onSelect: (SexChoice) -> Unit,
    onConfirm: () -> Unit,
) {
    QuestionScaffold(
        progress = QuestionProgress(current = 2, total = 7),
        title = strings.q2Title,
        subtitle = strings.q2Subtitle,
        onBack = onBack,
        primaryCta = {
            VoltButton(
                text = strings.continueAction,
                onClick = onConfirm,
                enabled = state.selected != null && !state.isSaving,
            )
        },
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            SexRow(
                choice = SexChoice.MALE,
                title = strings.q2OptionMaleTitle,
                subtitle = strings.q2OptionMaleSubtitle,
                selected = state.selected == SexChoice.MALE,
                onClick = onSelect,
                glyph = { sel -> MarsGlyph(selected = sel) },
            )
            SexRow(
                choice = SexChoice.FEMALE,
                title = strings.q2OptionFemaleTitle,
                subtitle = strings.q2OptionFemaleSubtitle,
                selected = state.selected == SexChoice.FEMALE,
                onClick = onSelect,
                glyph = { sel -> VenusGlyph(selected = sel) },
            )
            SexRow(
                choice = SexChoice.PREFER_NOT_TO_SAY,
                title = strings.q2OptionPreferNotTitle,
                subtitle = strings.q2OptionPreferNotSubtitle,
                selected = state.selected == SexChoice.PREFER_NOT_TO_SAY,
                onClick = onSelect,
                glyph = { sel -> EmptyCircleGlyph(selected = sel) },
            )
            Spacer(Modifier.height(0.dp))
        }
    }
}

@Composable
private fun SexRow(
    choice: SexChoice,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: (SexChoice) -> Unit,
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
// Zero-dep gender glyphs — circle + mars arrow / venus cross / plain.
// Glyph color = onVolt on a selected (volt-filled) tile, onSurface otherwise.
// ─────────────────────────────────────────────────────────────────────────────

private fun glyphColor(selected: Boolean): Color =
    if (selected) VoltColors.onVolt else VoltColors.onSurface

/** ♂ — circle bottom-left + arrow to upper-right. */
@Composable
private fun MarsGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 10f
        // Circle (lower-left, diameter ~55% of canvas)
        val d = w * 0.55f
        drawCircle(
            color = c,
            radius = d / 2f,
            center = Offset(w * 0.36f, h * 0.64f),
            style = Stroke(width = sw, cap = StrokeCap.Round),
        )
        // Arrow shaft from circle edge toward upper-right
        drawLine(
            color = c,
            start = Offset(w * 0.60f, h * 0.40f),
            end = Offset(w * 0.92f, h * 0.08f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Arrow head — two short legs pointing up-right
        drawLine(
            color = c,
            start = Offset(w * 0.92f, h * 0.08f),
            end = Offset(w * 0.66f, h * 0.08f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = c,
            start = Offset(w * 0.92f, h * 0.08f),
            end = Offset(w * 0.92f, h * 0.34f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

/** ♀ — circle top + cross extending below. */
@Composable
private fun VenusGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 10f
        // Circle (upper portion, diameter ~55% of canvas)
        val d = w * 0.55f
        drawCircle(
            color = c,
            radius = d / 2f,
            center = Offset(w * 0.50f, h * 0.36f),
            style = Stroke(width = sw, cap = StrokeCap.Round),
        )
        // Vertical stem below circle
        drawLine(
            color = c,
            start = Offset(w * 0.50f, h * 0.64f),
            end = Offset(w * 0.50f, h * 0.94f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Horizontal cross-bar
        drawLine(
            color = c,
            start = Offset(w * 0.32f, h * 0.80f),
            end = Offset(w * 0.68f, h * 0.80f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

/** Plain circle — "prefer not to say". */
@Composable
private fun EmptyCircleGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val sw = w / 10f
        drawCircle(
            color = c,
            radius = w * 0.40f,
            center = Offset(w / 2f, w / 2f),
            style = Stroke(width = sw, cap = StrokeCap.Round),
        )
    }
}
