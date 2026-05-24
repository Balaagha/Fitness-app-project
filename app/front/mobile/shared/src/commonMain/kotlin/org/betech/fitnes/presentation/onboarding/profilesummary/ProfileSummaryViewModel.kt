package org.betech.fitnes.presentation.onboarding.profilesummary

import androidx.lifecycle.ViewModel
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.datetime.Instant
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.model.ExperienceLevel
import org.betech.fitnes.domain.model.GoalType
import org.betech.fitnes.domain.model.OnboardingAnswer
import org.betech.fitnes.domain.model.Persona
import org.betech.fitnes.domain.model.PersonaResolver
import org.betech.fitnes.domain.model.Sex
import org.betech.fitnes.domain.model.TrainingContext
import org.betech.fitnes.domain.model.UserProfile
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.OnboardingRepository
import org.betech.fitnes.domain.repository.UserProfileRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Profile Summary VM (Orbit MVI) — Pencil u1cEVR.
 *
 * Loads the 7 stored answers from [OnboardingRepository] and projects them
 * into a `stepId → rawValue` string map. Localization is owned by the
 * Composable so the VM stays locale-agnostic.
 *
 * `CreateProfile`:
 *   1. Build [UserProfile] from the stored answers (best-effort — missing
 *      fields fall back to safe defaults so a deep-link demo never crashes).
 *   2. Resolve persona cell via [PersonaResolver].
 *   3. Persist via [UserProfileRepository.saveProfile].
 *   4. Emit `OnboardingCompleted(personaCell)` and route to AI Disclosure.
 */
class ProfileSummaryViewModel(
    private val onboardingRepo: OnboardingRepository,
    private val userProfileRepo: UserProfileRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<ProfileSummaryState, ProfileSummarySideEffect> {

    override val container =
        container<ProfileSummaryState, ProfileSummarySideEffect>(ProfileSummaryState())

    fun onIntent(i: ProfileSummaryIntent) = intent {
        when (i) {
            ProfileSummaryIntent.Load -> {
                try {
                    val progress = onboardingRepo.getProgress()
                    val raw = progress.answers
                        .mapValues { (_, answer) -> answer.asDisplayRaw() }
                        .toImmutableMap()
                    reduce { state.copy(answers = raw, isLoading = false) }
                } catch (t: Throwable) {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(ProfileSummarySideEffect.ShowError(t.message ?: "Error"))
                }
            }

            ProfileSummaryIntent.BackTapped ->
                postSideEffect(ProfileSummarySideEffect.NavigateBack)

            is ProfileSummaryIntent.EditRow ->
                postSideEffect(ProfileSummarySideEffect.NavigateToEdit(i.stepId))

            ProfileSummaryIntent.CreateProfile -> {
                reduce { state.copy(isCreating = true) }
                try {
                    val a = state.answers

                    val goal = a["q1_goal"]?.let { parseGoal(it) } ?: GoalType.GENERAL_FIT
                    val sex = a["q2_sex"]?.let { parseSex(it) } ?: Sex.MALE
                    val age = a["q3_age"]?.toDoubleOrNull()?.toInt() ?: 28
                    val heightCm = a["q4_height_cm"]?.toDoubleOrNull()?.toInt() ?: 175
                    val weightKg = a["q4_weight_kg"]?.toDoubleOrNull() ?: 75.0
                    val experience = a["q5_experience"]
                        ?.let { parseExperience(it) } ?: ExperienceLevel.BEGINNER
                    val context = a["q6_context"]
                        ?.let { parseContext(it) } ?: TrainingContext.HOME_ONLY
                    val weeklyDays = a["q7_weekly_days"]?.toDoubleOrNull()?.toInt() ?: 4
                    val sessionMin = a["q7_session_minutes"]?.toDoubleOrNull()?.toInt() ?: 45

                    val persona: Persona = PersonaResolver.resolve(context, sex, goal)
                    @OptIn(ExperimentalTime::class)
                    val now: Instant = Instant.parse(Clock.System.now().toString())

                    val profile = UserProfile(
                        userId = "local-pending",
                        email = null,
                        goal = goal,
                        sex = sex,
                        age = age,
                        heightCm = heightCm,
                        weightKg = weightKg,
                        experienceLevel = experience,
                        context = context,
                        weeklyDays = weeklyDays,
                        sessionDurationMin = sessionMin,
                        persona = persona,
                        // Refined on the AI Disclosure screen (Apple 2025 requirement).
                        aiDisclosureAcceptedAt = now,
                        createdAt = now,
                        updatedAt = now,
                    )

                    userProfileRepo.saveProfile(profile)
                    analytics.track(
                        AnalyticsEvent.OnboardingCompleted(personaCell = persona.cellId)
                    )

                    reduce { state.copy(isCreating = false) }
                    postSideEffect(ProfileSummarySideEffect.NavigateToAiDisclosure)
                } catch (t: Throwable) {
                    reduce { state.copy(isCreating = false) }
                    postSideEffect(ProfileSummarySideEffect.ShowError(t.message ?: "Error"))
                }
            }
        }
    }

    // ── Raw-value extraction (typed → string for state map) ──────────────────
    private fun OnboardingAnswer.asDisplayRaw(): String = when (this) {
        is OnboardingAnswer.SingleChoice -> value
        is OnboardingAnswer.MultiChoice -> values.joinToString(",")
        is OnboardingAnswer.Numeric ->
            if (value % 1.0 == 0.0) value.toInt().toString() else value.toString()
        is OnboardingAnswer.Slider -> value.toString()
        is OnboardingAnswer.DateOfBirth -> date.toString()
        is OnboardingAnswer.TextEntry -> text
        is OnboardingAnswer.BooleanAnswer -> value.toString()
        is OnboardingAnswer.HeightWeight -> "$heightCm,$weightKg"
        is OnboardingAnswer.DaysAndSession -> "$weeklyDays,$sessionDurationMin"
    }

    // ── Choice-string parsers (raw → schema enum) ────────────────────────────
    private fun parseGoal(raw: String): GoalType = when (raw.lowercase()) {
        "bulk" -> GoalType.BULK
        "cut" -> GoalType.CUT
        "general_fit" -> GoalType.GENERAL_FIT
        else -> GoalType.GENERAL_FIT
    }

    /** Q2 persists UI enum name (MALE/FEMALE/PREFER_NOT_TO_SAY). */
    private fun parseSex(raw: String): Sex = when (raw.uppercase()) {
        "MALE" -> Sex.MALE
        "FEMALE" -> Sex.FEMALE
        // PREFER_NOT_TO_SAY → binary schema fallback per Q2 doctrine.
        else -> Sex.MALE
    }

    private fun parseExperience(raw: String): ExperienceLevel = when (raw.lowercase()) {
        "beginner" -> ExperienceLevel.BEGINNER
        "intermediate" -> ExperienceLevel.INTERMEDIATE
        "advanced" -> ExperienceLevel.ADVANCED
        "athlete" -> ExperienceLevel.ATHLETE
        else -> ExperienceLevel.BEGINNER
    }

    /** Q6 persists UI enum name; 4 UI buckets collapse to 3 schema values. */
    private fun parseContext(raw: String): TrainingContext = when (raw.uppercase()) {
        "GYM" -> TrainingContext.SERIOUS_GYM
        "HYBRID" -> TrainingContext.CASUAL_GYM
        "HOME_BODYWEIGHT", "HOME_EQUIPMENT" -> TrainingContext.HOME_ONLY
        else -> TrainingContext.HOME_ONLY
    }
}
