package org.betech.fitnes.presentation.onboarding.trimesterpostpartum

/**
 * UI-only enum for the Trimester / Postpartum picker (Pencil C6Ya4A).
 * Persisted as a `single_choice` answer under stepId="trimester_postpartum"
 * (lowercase name); also fed into AnalyticsEvent.QuestionAnswered with the
 * raw UI value (T1/T2/T3/POSTPARTUM).
 *
 * Hard rule: this screen does NOT trigger AI plan generation
 * (CLAUDE.md: pregnancy_postpartum=true → curated static template only).
 */
enum class TrimesterChoice { T1, T2, T3, POSTPARTUM }

data class TrimesterPostpartumState(
    val selected: TrimesterChoice = TrimesterChoice.T2,
    val isSaving: Boolean = false,
)
