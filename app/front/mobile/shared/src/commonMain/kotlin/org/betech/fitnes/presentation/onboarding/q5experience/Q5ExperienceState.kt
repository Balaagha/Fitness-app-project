package org.betech.fitnes.presentation.onboarding.q5experience

/**
 * UI-only experience enum for Q5 (Pencil i1Vu9). Mirrors the
 * schema-canonical [org.betech.fitnes.domain.model.ExperienceLevel] 1:1 —
 * decoupled so the screen can grow design buckets without churning the
 * domain enum.
 */
enum class ExperienceChoice { BEGINNER, INTERMEDIATE, ADVANCED, ATHLETE }

/**
 * Q5 Experience screen state. Defaults to [ExperienceChoice.BEGINNER] —
 * the design preselects the most conservative tier so a tap-through user
 * still gets a safe plan.
 */
data class Q5ExperienceState(
    val selected: ExperienceChoice? = ExperienceChoice.BEGINNER,
    val isSaving: Boolean = false,
)
