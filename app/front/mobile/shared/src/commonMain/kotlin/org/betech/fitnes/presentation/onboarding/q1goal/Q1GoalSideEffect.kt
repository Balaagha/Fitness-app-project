package org.betech.fitnes.presentation.onboarding.q1goal

sealed interface Q1GoalSideEffect {
    data object NavigateBack : Q1GoalSideEffect
    data object NavigateToQ2Sex : Q1GoalSideEffect
    data class ShowError(val message: String) : Q1GoalSideEffect
}
