package org.betech.fitnes.presentation.onboarding.emaillogin

sealed interface EmailLoginSideEffect {
    data object NavigateBack : EmailLoginSideEffect
    data object NavigateToForgotPassword : EmailLoginSideEffect
    data object NavigateToSignup : EmailLoginSideEffect
    data object NavigateToPaywall : EmailLoginSideEffect
}
