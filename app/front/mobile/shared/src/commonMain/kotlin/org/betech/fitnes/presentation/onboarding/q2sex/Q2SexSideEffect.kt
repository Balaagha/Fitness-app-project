package org.betech.fitnes.presentation.onboarding.q2sex

sealed interface Q2SexSideEffect {
    data object NavigateBack : Q2SexSideEffect
    data object NavigateToQ3Age : Q2SexSideEffect
    data class ShowError(val message: String) : Q2SexSideEffect
}
