package org.betech.fitnes.presentation.onboarding.q6context

/**
 * UI-only context enum for Q6 (Pencil F16e8). The schema-canonical
 * [org.betech.fitnes.domain.model.TrainingContext] is 3-valued
 * (SERIOUS_GYM / CASUAL_GYM / HOME_ONLY), while design exposes 4 buckets
 * for clarity at onboarding time. Mapping happens at save (see VM).
 *
 * Persisted as `OnboardingAnswer.SingleChoice` with the UI enum name so
 * we never lose the user-visible distinction (HOME_BODYWEIGHT vs
 * HOME_EQUIPMENT collapses to HOME_ONLY in the canonical column).
 */
enum class ContextChoice { HOME_BODYWEIGHT, HOME_EQUIPMENT, GYM, HYBRID }

/**
 * Q6 Context screen state. Defaults to [ContextChoice.HOME_BODYWEIGHT] —
 * the safest assumption (no equipment) for a tap-through user.
 */
data class Q6ContextState(
    val selected: ContextChoice? = ContextChoice.HOME_BODYWEIGHT,
    val isSaving: Boolean = false,
)
