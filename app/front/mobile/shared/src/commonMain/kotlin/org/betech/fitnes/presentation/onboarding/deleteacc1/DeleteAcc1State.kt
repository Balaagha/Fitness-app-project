package org.betech.fitnes.presentation.onboarding.deleteacc1

/**
 * Delete Account · Step 1 (Pencil e74FR) — pure reassurance/disclosure step.
 * No async work, no toggles → state is intentionally empty (kept as a data
 * class so future fields can be added without breaking VM signature).
 */
data class DeleteAcc1State(
    val placeholder: Unit = Unit,
)
