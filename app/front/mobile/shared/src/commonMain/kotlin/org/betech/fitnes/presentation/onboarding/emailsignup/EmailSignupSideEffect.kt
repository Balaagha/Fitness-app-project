package org.betech.fitnes.presentation.onboarding.emailsignup

sealed interface EmailSignupSideEffect {
    data object NavigateBack : EmailSignupSideEffect
    data object NavigateToEmailVerify : EmailSignupSideEffect
    data object NavigateToEmailLogin : EmailSignupSideEffect
}
