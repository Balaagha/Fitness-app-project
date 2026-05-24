package org.betech.fitnes.presentation.onboarding.q7daysession

sealed interface Q7DaySessionSideEffect {
    data object NavigateBack : Q7DaySessionSideEffect
    data object NavigateToProfileSummary : Q7DaySessionSideEffect
    data class ShowError(val message: String) : Q7DaySessionSideEffect
}
