package org.betech.fitnes.presentation.onboarding.signupemailexists

sealed interface SignupEmailExistsIntent {
    data class EmailChanged(val v: String) : SignupEmailExistsIntent
    data class PasswordChanged(val v: String) : SignupEmailExistsIntent
    data class ConfirmChanged(val v: String) : SignupEmailExistsIntent
    data object BackTapped : SignupEmailExistsIntent
    data object GoLoginTapped : SignupEmailExistsIntent
    data object PrivacyTapped : SignupEmailExistsIntent
}
