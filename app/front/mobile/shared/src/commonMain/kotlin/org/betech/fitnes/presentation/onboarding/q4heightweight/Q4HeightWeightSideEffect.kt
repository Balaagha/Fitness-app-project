package org.betech.fitnes.presentation.onboarding.q4heightweight

/** Orbit side-effects for Q4. */
sealed interface Q4HeightWeightSideEffect {
    data object NavigateBack : Q4HeightWeightSideEffect
    data object NavigateToQ5Experience : Q4HeightWeightSideEffect
    data class ShowError(val message: String) : Q4HeightWeightSideEffect
}
