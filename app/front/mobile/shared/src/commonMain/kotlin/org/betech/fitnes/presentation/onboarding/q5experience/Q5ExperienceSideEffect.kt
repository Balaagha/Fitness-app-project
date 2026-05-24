package org.betech.fitnes.presentation.onboarding.q5experience

sealed interface Q5ExperienceSideEffect {
    data object NavigateBack : Q5ExperienceSideEffect
    data object NavigateToQ6Context : Q5ExperienceSideEffect
    data class ShowError(val message: String) : Q5ExperienceSideEffect
}
