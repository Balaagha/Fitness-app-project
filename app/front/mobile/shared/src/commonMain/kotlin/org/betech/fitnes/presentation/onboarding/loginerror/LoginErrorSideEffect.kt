package org.betech.fitnes.presentation.onboarding.loginerror

sealed interface LoginErrorSideEffect {
    data object NavigateBack : LoginErrorSideEffect
    data object NavigateToForgotPassword : LoginErrorSideEffect
}
