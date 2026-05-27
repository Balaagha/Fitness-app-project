package org.betech.fitnes.presentation.onboarding.q3age

sealed interface Q3AgeSideEffect {
    data object NavigateBack : Q3AgeSideEffect
    data object NavigateToQ4HeightWeight : Q3AgeSideEffect

    /** Routed when answer falls outside [Q3AgeState.SOFT_MIN_AGE]..[Q3AgeState.SOFT_MAX_AGE]. */
    data object NavigateToSoftWarning : Q3AgeSideEffect

    data class ShowError(val message: String) : Q3AgeSideEffect
}
