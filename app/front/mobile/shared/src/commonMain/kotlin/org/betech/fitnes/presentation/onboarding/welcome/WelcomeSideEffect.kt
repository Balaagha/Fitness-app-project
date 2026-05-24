package org.betech.fitnes.presentation.onboarding.welcome

sealed interface WelcomeSideEffect {
    data class AdvanceTo(val index: Int) : WelcomeSideEffect
    data object NavigateToQ1Goal : WelcomeSideEffect
    data object NavigateToLogin : WelcomeSideEffect
    data object NavigateToLanguageSelect : WelcomeSideEffect
}
