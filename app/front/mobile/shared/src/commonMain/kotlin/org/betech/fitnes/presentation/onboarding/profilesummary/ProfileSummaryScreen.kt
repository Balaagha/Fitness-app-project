package org.betech.fitnes.presentation.onboarding.profilesummary

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.betech.fitnes.designsystem.color.VoltColors
import org.betech.fitnes.designsystem.components.VoltButton
import org.betech.fitnes.designsystem.components.VoltButtonSecondary
import org.betech.fitnes.designsystem.typography.VoltType
import org.betech.fitnes.localization.LocalStrings
import org.betech.fitnes.localization.Strings
import org.betech.fitnes.presentation.onboarding.aidisclosure.AiDisclosureScreen
import org.betech.fitnes.presentation.onboarding.q1goal.Q1GoalScreen
import org.betech.fitnes.presentation.onboarding.q2sex.Q2SexScreen
import org.betech.fitnes.presentation.onboarding.q3age.Q3AgeScreen
import org.betech.fitnes.presentation.onboarding.q4heightweight.Q4HeightWeightScreen
import org.betech.fitnes.presentation.onboarding.q5experience.Q5ExperienceScreen
import org.betech.fitnes.presentation.onboarding.q6context.Q6ContextScreen
import org.betech.fitnes.presentation.onboarding.q7daysession.Q7DaySessionScreen
import org.koin.compose.viewmodel.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

/**
 * 12 · Profile Summary (Pencil u1cEVR · "Hər şey doğrudurmu?").
 *
 * Post-Q7 review screen. 7 rows ([label] + [value] + edit pencil), info notice,
 * "Profilimi yarat" (persists [UserProfile] → routes to AI Disclosure) +
 * "Geriyə dön" secondary CTA.
 *
 * Layout doctrine:
 *  - NO progress bar (onboarding-question phase is over).
 *  - Custom top bar — back chevron + eyebrow + title only.
 *  - Rows are `surface1` cards (56dp tall, 16dp corner, 12dp gap).
 *  - Edit pencil = 24dp tap-target, `onSurfaceMuted` tint.
 */
class ProfileSummaryScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val viewModel: ProfileSummaryViewModel = koinViewModel()
        val state by viewModel.collectAsState()
        val strings = LocalStrings.current

        LaunchedEffect(Unit) { viewModel.onIntent(ProfileSummaryIntent.Load) }

        viewModel.collectSideEffect { effect ->
            when (effect) {
                ProfileSummarySideEffect.NavigateBack -> {
                    if (!navigator.pop()) navigator.replace(Q7DaySessionScreen())
                }
                is ProfileSummarySideEffect.NavigateToEdit -> {
                    val target: Screen = when (effect.stepId) {
                        "q1_goal" -> Q1GoalScreen()
                        "q2_sex" -> Q2SexScreen()
                        "q3_age" -> Q3AgeScreen()
                        "q4_height_weight" -> Q4HeightWeightScreen()
                        "q5_experience" -> Q5ExperienceScreen()
                        "q6_context" -> Q6ContextScreen()
                        "q7_day_session" -> Q7DaySessionScreen()
                        else -> Q1GoalScreen()
                    }
                    navigator.push(target)
                }
                ProfileSummarySideEffect.NavigateToAiDisclosure ->
                    navigator.push(AiDisclosureScreen())
                is ProfileSummarySideEffect.ShowError -> Unit // toast hook later
            }
        }

        ProfileSummaryContent(
            state = state,
            strings = strings,
            onBack = { viewModel.onIntent(ProfileSummaryIntent.BackTapped) },
            onEdit = { viewModel.onIntent(ProfileSummaryIntent.EditRow(it)) },
            onCreate = { viewModel.onIntent(ProfileSummaryIntent.CreateProfile) },
        )
    }
}

@Composable
private fun ProfileSummaryContent(
    state: ProfileSummaryState,
    strings: Strings,
    onBack: () -> Unit,
    onEdit: (String) -> Unit,
    onCreate: () -> Unit,
) {
    val rows = buildRows(state, strings)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(VoltColors.surface0),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(horizontal = 24.dp),
        ) {
            // ── Top bar (back only — onboarding question phase is over) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                BackChevronButton(onClick = onBack)
            }

            Spacer(Modifier.height(16.dp))

            // ── Eyebrow + title ────────────────────────────────────────
            Text(
                text = strings.profileSummaryEyebrow,
                style = VoltType.labelLarge.copy(
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp,
                ),
                color = VoltColors.onSurfaceMuted,
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = strings.profileSummaryTitle,
                style = VoltType.displayMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                ),
                color = VoltColors.onSurface,
            )

            Spacer(Modifier.height(24.dp))

            // ── Scrollable row stack ───────────────────────────────────
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                rows.forEach { row ->
                    SummaryRow(
                        label = row.label,
                        value = row.value,
                        onEdit = { onEdit(row.editTargetStepId) },
                    )
                }

                Spacer(Modifier.height(12.dp))
                InfoNotice(text = strings.profileSummaryNotice)
            }

            Spacer(Modifier.height(16.dp))

            // ── CTAs ───────────────────────────────────────────────────
            VoltButton(
                text = strings.profileSummaryCtaCreate,
                onClick = onCreate,
                enabled = !state.isCreating,
            )
            Spacer(Modifier.height(8.dp))
            VoltButtonSecondary(
                text = strings.profileSummaryCtaBack,
                onClick = onBack,
                enabled = !state.isCreating,
            )

            Spacer(Modifier.height(24.dp))
        }
    }
}

// ──────────────────────────────────────────────────────────────────────────────
// Row helpers
// ──────────────────────────────────────────────────────────────────────────────

private data class DisplayRow(
    val editTargetStepId: String,
    val label: String,
    val value: String,
)

/**
 * Project the raw answer map into 7 localized rows.
 *
 * `editTargetStepId` is a virtual id distinct from the storage stepIds —
 * Q4 stores two cells (`q4_height_cm` + `q4_weight_kg`) but the edit
 * affordance jumps to the single Q4 screen, hence `q4_height_weight`.
 * Same pattern for Q7 (`q7_day_session`).
 */
@Composable
private fun buildRows(state: ProfileSummaryState, s: Strings): List<DisplayRow> {
    val a = state.answers
    val empty = s.profileSummaryEmpty

    val goalValue = a["q1_goal"]?.let { goalLabel(it, s) } ?: empty
    val sexValue = a["q2_sex"]?.let { sexLabel(it, s) } ?: empty
    val ageValue = a["q3_age"] ?: empty
    val hwValue = run {
        val h = a["q4_height_cm"]?.toDoubleOrNull()?.toInt()
        val w = a["q4_weight_kg"]?.toDoubleOrNull()
        if (h != null && w != null) s.profileHeightWeightValue(h, w) else empty
    }
    val expValue = a["q5_experience"]?.let { experienceLabel(it, s) } ?: empty
    val ctxValue = a["q6_context"]?.let { contextLabel(it, s) } ?: empty
    val schedValue = run {
        val d = a["q7_weekly_days"]?.toDoubleOrNull()?.toInt()
        val m = a["q7_session_minutes"]?.toDoubleOrNull()?.toInt()
        if (d != null && m != null) s.q7Summary(d, m) else empty
    }

    return listOf(
        DisplayRow("q1_goal", s.profileRowGoal, goalValue),
        DisplayRow("q2_sex", s.profileRowSex, sexValue),
        DisplayRow("q3_age", s.profileRowAge, ageValue),
        DisplayRow("q4_height_weight", s.profileRowHeightWeight, hwValue),
        DisplayRow("q5_experience", s.profileRowExperience, expValue),
        DisplayRow("q6_context", s.profileRowContext, ctxValue),
        DisplayRow("q7_day_session", s.profileRowSchedule, schedValue),
    )
}

private fun goalLabel(raw: String, s: Strings): String = when (raw.lowercase()) {
    // q1_goal persists GoalType.lowercase (bulk/cut/general_fit).
    "cut" -> s.q1OptionLoseFatTitle
    "bulk" -> s.q1OptionBuildMuscleTitle
    "general_fit" -> s.q1OptionGetTonedTitle
    // Legacy/UI-bucket fallbacks (in case raw value is a UI enum name).
    "lose_fat" -> s.q1OptionLoseFatTitle
    "build_muscle" -> s.q1OptionBuildMuscleTitle
    "get_toned" -> s.q1OptionGetTonedTitle
    "increase_strength" -> s.q1OptionIncreaseStrengthTitle
    else -> raw
}

private fun sexLabel(raw: String, s: Strings): String = when (raw.uppercase()) {
    "MALE" -> s.q2OptionMaleTitle
    "FEMALE" -> s.q2OptionFemaleTitle
    "PREFER_NOT_TO_SAY" -> s.q2OptionPreferNotTitle
    else -> raw
}

private fun experienceLabel(raw: String, s: Strings): String = when (raw.lowercase()) {
    "beginner" -> s.q5OptionBeginnerTitle
    "intermediate" -> s.q5OptionIntermediateTitle
    "advanced" -> s.q5OptionAdvancedTitle
    "athlete" -> s.q5OptionAthleteTitle
    else -> raw
}

private fun contextLabel(raw: String, s: Strings): String = when (raw.uppercase()) {
    "HOME_BODYWEIGHT" -> s.q6OptionHomeBodyweightTitle
    "HOME_EQUIPMENT" -> s.q6OptionHomeEquipmentTitle
    "GYM" -> s.q6OptionGymTitle
    "HYBRID" -> s.q6OptionHybridTitle
    else -> raw
}

// ──────────────────────────────────────────────────────────────────────────────
// Atoms
// ──────────────────────────────────────────────────────────────────────────────

@Composable
private fun SummaryRow(label: String, value: String, onEdit: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(VoltColors.surface1)
            .border(1.dp, VoltColors.outline, RoundedCornerShape(16.dp))
            .clickable(onClick = onEdit)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(
            text = label,
            style = VoltType.bodyMedium.copy(fontSize = 14.sp),
            color = VoltColors.onSurfaceMuted,
            modifier = Modifier.weight(1f, fill = false),
        )
        Spacer(Modifier.weight(1f))
        Text(
            text = value,
            style = VoltType.bodyMedium.copy(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = VoltColors.onSurface,
        )
        EditPencilButton(onClick = onEdit)
    }
}

/** 24dp pencil glyph — `onSurfaceMuted`, vector-drawn (zero-dep). */
@Composable
private fun EditPencilButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(14.dp)) { drawPencil() }
    }
}

private fun DrawScope.drawPencil() {
    val color = VoltColors.onSurfaceMuted
    val sw = size.minDimension / 8f
    // Diagonal shaft from top-right → bottom-left.
    drawLine(
        color = color,
        start = Offset(size.width * 0.85f, size.height * 0.15f),
        end = Offset(size.width * 0.20f, size.height * 0.80f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
    // Tip notch (small line at bottom-left).
    drawLine(
        color = color,
        start = Offset(size.width * 0.10f, size.height * 0.90f),
        end = Offset(size.width * 0.30f, size.height * 0.70f),
        strokeWidth = sw,
        cap = StrokeCap.Round,
    )
}

@Composable
private fun InfoNotice(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(VoltColors.volt.copy(alpha = 0.10f))
            .border(1.dp, VoltColors.volt, RoundedCornerShape(12.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = text,
            style = VoltType.bodyMedium.copy(fontSize = 13.sp),
            color = VoltColors.onSurface,
        )
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
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
    drawLine(
        color = VoltColors.onSurface,
        start = Offset(left, midY),
        end = Offset(right, size.height),
        strokeWidth = strokeWidth,
        cap = StrokeCap.Round,
    )
}
