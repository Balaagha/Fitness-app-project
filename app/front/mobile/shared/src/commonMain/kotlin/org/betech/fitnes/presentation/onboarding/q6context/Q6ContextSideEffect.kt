package org.betech.fitnes.presentation.onboarding.q6context

sealed interface Q6ContextSideEffect {
    data object NavigateBack : Q6ContextSideEffect
    data object NavigateToQ7DaySession : Q6ContextSideEffect
    data class ShowError(val message: String) : Q6ContextSideEffect
}
