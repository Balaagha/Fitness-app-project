package org.betech.fitnes.presentation.onboarding.q3age

/**
 * Q3 · Age — Orbit MVI state.
 *
 * Range = 13..90. Default 25. `isValid` always true at default,
 * but kept explicit for symmetry with sibling Q-screens.
 *
 * NOTE: V5 · Q3 soft-warning (Pencil iNSs8) handled in a later
 * iteration. For now, out-of-suggested-range values (age<16 || age>65)
 * are accepted without branching.
 */
data class Q3AgeState(
    val age: Int = 25,
    val showInputDialog: Boolean = false,
    val isSaving: Boolean = false,
) {
    val isValid: Boolean get() = age in MIN_AGE..MAX_AGE

    companion object {
        const val MIN_AGE: Int = 13
        const val MAX_AGE: Int = 90
    }
}
