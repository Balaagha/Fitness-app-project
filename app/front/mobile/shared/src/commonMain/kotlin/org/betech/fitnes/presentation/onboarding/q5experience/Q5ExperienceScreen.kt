package org.betech.fitnes.presentation.onboarding.q5experience

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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
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
import org.betech.fitnes.presentation.onboarding.q4heightweight.Q4HeightWeightScreen
import org.betech.fitnes.presentation.onboarding.q6context.Q6ContextScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 1.5 · Q5 Experience (Pencil i1Vu9 · "Q5 · Təcrübə").
 *
 * Step 5 of 7. Single-choice over 4 buckets that map 1:1 to
 * [org.betech.fitnes.domain.model.ExperienceLevel] (BEGINNER, INTERMEDIATE,
 * ADVANCED, ATHLETE). Default = BEGINNER (safest plan for tap-through).
 *
 * New design pattern: a tiny eyebrow label ("5 / 7 · TƏCRÜBƏ") sits at the
 * top of the content slot — rendered inline rather than threading a new
 * optional parameter through [QuestionScaffold] so Q1–Q4 stay untouched.
 */
class Q5ExperienceScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: Q5ExperienceViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        viewModel.collectSideEffect { effect ->
            when (effect) {
                Q5ExperienceSideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(Q4HeightWeightScreen())
                }
                Q5ExperienceSideEffect.NavigateToQ6Context -> navigator.push(Q6ContextScreen())
                is Q5ExperienceSideEffect.ShowError -> Unit // toast hook later
            }
        }

        Q5ExperienceContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(Q5ExperienceIntent.BackTapped) },
            onSelect = { viewModel.onIntent(Q5ExperienceIntent.Select(it)) },
            onConfirm = { viewModel.onIntent(Q5ExperienceIntent.Confirm) },
        )
    }
}

@Composable
private fun Q5ExperienceContent(
    state: Q5ExperienceState,
    strings: Strings,
    onBack: () -> Unit,
    onSelect: (ExperienceChoice) -> Unit,
    onConfirm: () -> Unit,
) {
    QuestionScaffold(
        progress = QuestionProgress(current = 5, total = 7),
        title = strings.q5Title,
        subtitle = strings.q5Subtitle,
        onBack = onBack,
        primaryCta = {
            VoltButton(
                text = strings.continueAction,
                onClick = onConfirm,
                enabled = state.selected != null && !state.isSaving,
            )
        },
    ) {
        // Eyebrow — inlined at top of content slot (not in scaffold) so
        // Q1–Q4 stay backward-compatible. 10sp · 700 · letterSpacing 2sp.
        Text(
            text = strings.q5Eyebrow,
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
            ExperienceRow(
                choice = ExperienceChoice.BEGINNER,
                title = strings.q5OptionBeginnerTitle,
                subtitle = strings.q5OptionBeginnerSubtitle,
                selected = state.selected == ExperienceChoice.BEGINNER,
                onClick = onSelect,
                glyph = { sel -> SeedlingGlyph(selected = sel) },
            )
            ExperienceRow(
                choice = ExperienceChoice.INTERMEDIATE,
                title = strings.q5OptionIntermediateTitle,
                subtitle = strings.q5OptionIntermediateSubtitle,
                selected = state.selected == ExperienceChoice.INTERMEDIATE,
                onClick = onSelect,
                glyph = { sel -> DumbbellGlyph(selected = sel) },
            )
            ExperienceRow(
                choice = ExperienceChoice.ADVANCED,
                title = strings.q5OptionAdvancedTitle,
                subtitle = strings.q5OptionAdvancedSubtitle,
                selected = state.selected == ExperienceChoice.ADVANCED,
                onClick = onSelect,
                glyph = { sel -> GradCapGlyph(selected = sel) },
            )
            ExperienceRow(
                choice = ExperienceChoice.ATHLETE,
                title = strings.q5OptionAthleteTitle,
                subtitle = strings.q5OptionAthleteSubtitle,
                selected = state.selected == ExperienceChoice.ATHLETE,
                onClick = onSelect,
                glyph = { sel -> TrophyGlyph(selected = sel) },
            )
            Spacer(Modifier.height(0.dp))
        }
    }
}

@Composable
private fun ExperienceRow(
    choice: ExperienceChoice,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: (ExperienceChoice) -> Unit,
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
// ─────────────────────────────────────────────────────────────────────────────

private fun glyphColor(selected: Boolean): Color =
    if (selected) VoltColors.onVolt else VoltColors.onSurface

/** Seedling — a small upward triangle leaf on a short stem. */
@Composable
private fun SeedlingGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(20.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 8f
        // Stem
        drawLine(
            color = c,
            start = Offset(w / 2f, h * 0.55f),
            end = Offset(w / 2f, h * 0.90f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Leaf — small triangle pointing up
        val leaf = Path().apply {
            moveTo(w * 0.50f, h * 0.10f)
            lineTo(w * 0.18f, h * 0.55f)
            lineTo(w * 0.82f, h * 0.55f)
            close()
        }
        drawPath(path = leaf, color = c, style = Stroke(width = sw, cap = StrokeCap.Round))
    }
}

/** Dumbbell — bar with end caps. */
@Composable
private fun DumbbellGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 7f
        // Bar
        drawLine(
            color = c,
            start = Offset(w * 0.28f, h * 0.50f),
            end = Offset(w * 0.72f, h * 0.50f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Left cap
        drawLine(
            color = c,
            start = Offset(w * 0.20f, h * 0.32f),
            end = Offset(w * 0.20f, h * 0.68f),
            strokeWidth = sw * 1.8f,
            cap = StrokeCap.Round,
        )
        // Right cap
        drawLine(
            color = c,
            start = Offset(w * 0.80f, h * 0.32f),
            end = Offset(w * 0.80f, h * 0.68f),
            strokeWidth = sw * 1.8f,
            cap = StrokeCap.Round,
        )
    }
}

/** Graduation cap — trapezoid + tassel. */
@Composable
private fun GradCapGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 9f
        // Mortarboard — flat diamond outline
        val board = Path().apply {
            moveTo(w * 0.10f, h * 0.42f)
            lineTo(w * 0.50f, h * 0.22f)
            lineTo(w * 0.90f, h * 0.42f)
            lineTo(w * 0.50f, h * 0.62f)
            close()
        }
        drawPath(path = board, color = c, style = Stroke(width = sw, cap = StrokeCap.Round))
        // Tassel line
        drawLine(
            color = c,
            start = Offset(w * 0.82f, h * 0.46f),
            end = Offset(w * 0.82f, h * 0.78f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Tassel dot
        drawCircle(color = c, radius = sw * 0.9f, center = Offset(w * 0.82f, h * 0.84f))
    }
}

/** Trophy — chalice silhouette (cup + stem + base). */
@Composable
private fun TrophyGlyph(selected: Boolean) {
    val c = glyphColor(selected)
    Canvas(modifier = Modifier.size(22.dp)) {
        val w = size.width
        val h = size.height
        val sw = w / 10f
        // Cup
        val cup = Path().apply {
            moveTo(w * 0.25f, h * 0.15f)
            lineTo(w * 0.75f, h * 0.15f)
            lineTo(w * 0.65f, h * 0.55f)
            lineTo(w * 0.35f, h * 0.55f)
            close()
        }
        drawPath(path = cup, color = c, style = Stroke(width = sw, cap = StrokeCap.Round))
        // Handles — small arcs on either side
        drawArc(
            color = c,
            startAngle = 270f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(w * 0.05f, h * 0.20f),
            size = Size(w * 0.20f, h * 0.25f),
            style = Stroke(width = sw, cap = StrokeCap.Round),
        )
        drawArc(
            color = c,
            startAngle = 270f,
            sweepAngle = -180f,
            useCenter = false,
            topLeft = Offset(w * 0.75f, h * 0.20f),
            size = Size(w * 0.20f, h * 0.25f),
            style = Stroke(width = sw, cap = StrokeCap.Round),
        )
        // Stem
        drawLine(
            color = c,
            start = Offset(w * 0.50f, h * 0.55f),
            end = Offset(w * 0.50f, h * 0.78f),
            strokeWidth = sw,
            cap = StrokeCap.Round,
        )
        // Base
        drawLine(
            color = c,
            start = Offset(w * 0.30f, h * 0.88f),
            end = Offset(w * 0.70f, h * 0.88f),
            strokeWidth = sw * 1.4f,
            cap = StrokeCap.Round,
        )
    }
}
