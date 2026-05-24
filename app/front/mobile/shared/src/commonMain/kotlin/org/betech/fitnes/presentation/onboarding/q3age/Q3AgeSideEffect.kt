package org.betech.fitnes.presentation.onboarding.q3age

sealed interface Q3AgeSideEffect {
    data object NavigateBack : Q3AgeSideEffect
    data object NavigateToQ4HeightWeight : Q3AgeSideEffect
    data class ShowError(val message: String) : Q3AgeSideEffect
}
