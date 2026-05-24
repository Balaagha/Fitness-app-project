package org.betech.fitnes.presentation.onboarding.signupemailexists

sealed interface SignupEmailExistsSideEffect {
    data object NavigateBack : SignupEmailExistsSideEffect
    data object NavigateToEmailLogin : SignupEmailExistsSideEffect
    data object ShowPrivacyToast : SignupEmailExistsSideEffect
}
