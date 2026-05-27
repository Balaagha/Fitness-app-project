package org.betech.fitnes.presentation.onboarding.q3age

/**
 * Q3 · Age — Orbit MVI state.
 *
 * Hard range = [MIN_AGE]..[MAX_AGE] (13..90). Default 25.
 * `isValid` always true at default, kept explicit for symmetry with
 * sibling Q-screens.
 *
 * Suggested range = [SOFT_MIN_AGE]..[SOFT_MAX_AGE] (16..65). Out of
 * the suggested range the VM routes to `Q3AgeSoftWarningScreen` instead
 * of Q4 — non-blocking informational interstitial (Pencil V5 · iNSs8).
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

        /** Suggested-range lower bound — below this triggers soft warning. */
        const val SOFT_MIN_AGE: Int = 16

        /** Suggested-range upper bound — above this triggers soft warning. */
        const val SOFT_MAX_AGE: Int = 65
    }
}
