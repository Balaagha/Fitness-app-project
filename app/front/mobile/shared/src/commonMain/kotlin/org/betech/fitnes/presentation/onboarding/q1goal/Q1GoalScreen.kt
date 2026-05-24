package org.betech.fitnes.presentation.onboarding.q1goal

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
import org.betech.fitnes.presentation.onboarding.q2sex.Q2SexScreen
import org.betech.fitnes.presentation.onboarding.welcome.WelcomeScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 1.1 · Q1 Goal (Pencil S5QT23 · "Q1 · Hədəf").
 *
 * Step 1 of 7 mandatory onboarding questions. Single-choice over 4 UI buckets
 * that map to the 3-value schema [org.betech.fitnes.domain.model.GoalType].
 */
class Q1GoalScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q1GoalViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q1GoalSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(WelcomeScreen())
                }
                Q1GoalSideEffect.NavigateToQ2Sex -> navigator.push(Q2SexScreen())
                is Q1GoalSideEffect.ShowError -> Unit // toast hook later
            }
        }

        Q1GoalContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(Q1GoalIntent.BackTapped) },
            onSelect = { viewModel.onIntent(Q1GoalIntent.Select(it)) },
            onConfirm = { viewModel.onIntent(Q1GoalIntent.Confirm) },
        )
    }
}

@Composable
private fun Q1GoalContent(
    state: Q1GoalState,
    strings: Strings,
    onBack: () -> Unit,
    onSelect: (GoalChoice) -> Unit,
    onConfirm: () -> Unit,
) {
    QuestionScaffold(
        progress = QuestionProgress(current = 1, total = 7),
        title = strings.q1Title,
        subtitle = strings.q1Subtitle,
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
            GoalRow(
                choice = GoalChoice.LOSE_FAT,
                title = strings.q1OptionLoseFatTitle,
                subtitle = strings.q1OptionLoseFatSubtitle,
                selected = state.selected == GoalChoice.LOSE_FAT,
                onClick = onSelect,
                glyph = { sel -> DownArrowGlyph(selected = sel) },
            )
            GoalRow(
                choice = GoalChoice.BUILD_MUSCLE,
                title = strings.q1OptionBuildMuscleTitle,
                subtitle = strings.q1OptionBuildMuscleSubtitle,
                selected = state.selected == GoalChoice.BUILD_MUSCLE,
                onClick = onSelect,
                glyph = { sel -> UpArrowGlyph(selected = sel) },
            )
            GoalRow(
                choice = GoalChoice.GET_TONED,
                title = strings.q1OptionGetTonedTitle,
                subtitle = strings.q1OptionGetTonedSubtitle,
                selected = state.selected == GoalChoice.GET_TONED,
                onClick = onSelect,
                glyph = { sel -> BalanceGlyph(selected = sel) },
            )
            GoalRow(
                choice = GoalChoice.INCREASE_STRENGTH,
                title = strings.q1OptionIncreaseStrengthTitle,
                subtitle = strings.q1OptionIncreaseStrengthSubtitle,
                selected = state.selected == GoalChoice.INCREASE_STRENGTH,
                onClick = onSelect,
                glyph = { sel -> BarbellGlyph(selected = sel) },
            )
            Spacer(Modifier.height(0.dp))
        }
    }
}

@Composable
private fun GoalRow(
    choice: GoalChoice,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: (GoalChoice) -> Unit,
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
// Zero-dep glyphs — simple geometric shapes per design note.
// Glyph color = onVolt on a selected (volt-filled) tile, onSurface otherwise.
// ─────────────────────────────────────────────────────────────────────────────

private fun glyphColor(selected: Boolean): Color =
    if (selected) VoltColors.onVolt else VoltColors.onSurface

@Composable
private fun DownArrowGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 7f
        // Vertical shaft
        drawLine(
            color = c,
            start = Offset(w / 2f, h * 0.15f),
            end = Offset(w / 2f, h * 0.78f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Arrow head (V shape)
        drawLine(
            color = c,
            start = Offset(w * 0.22f, h * 0.58f),
            end = Offset(w / 2f, h * 0.88f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = c,
            start = Offset(w * 0.78f, h * 0.58f),
            end = Offset(w / 2f, h * 0.88f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun UpArrowGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 7f
        drawLine(
            color = c,
            start = Offset(w / 2f, h * 0.22f),
            end = Offset(w / 2f, h * 0.85f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = c,
            start = Offset(w * 0.22f, h * 0.42f),
            end = Offset(w / 2f, h * 0.12f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        drawLine(
            color = c,
            start = Offset(w * 0.78f, h * 0.42f),
            end = Offset(w / 2f, h * 0.12f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun BalanceGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 8f
        // Horizontal balance beam
        drawLine(
            color = c,
            start = Offset(w * 0.10f, h * 0.50f),
            end = Offset(w * 0.90f, h * 0.50f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Left tray
        drawLine(
            color = c,
            start = Offset(w * 0.20f, h * 0.50f),
            end = Offset(w * 0.20f, h * 0.78f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Right tray
        drawLine(
            color = c,
            start = Offset(w * 0.80f, h * 0.50f),
            end = Offset(w * 0.80f, h * 0.78f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Center post
        drawLine(
            color = c,
            start = Offset(w * 0.50f, h * 0.50f),
            end = Offset(w * 0.50f, h * 0.20f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
    }
}

@Composable
private fun BarbellGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 7f
        // Bar
        drawLine(
            color = c,
            start = Offset(w * 0.18f, h * 0.50f),
            end = Offset(w * 0.82f, h * 0.50f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Left plate
        drawLine(
            color = c,
            start = Offset(w * 0.20f, h * 0.22f),
            end = Offset(w * 0.20f, h * 0.78f),
            strokeWidth = sw * 1.6f,
            cap = StrokeCap.Round,
        )
        // Right plate
        drawLine(
            color = c,
            start = Offset(w * 0.80f, h * 0.22f),
            end = Offset(w * 0.80f, h * 0.78f),
            strokeWidth = sw * 1.6f,
            cap = StrokeCap.Round,
        )
    }
}
