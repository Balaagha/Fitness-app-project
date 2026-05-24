package org.betech.fitnes.presentation.onboarding.emailsignup

sealed interface EmailSignupIntent {
    data class EmailChanged(val v: String) : EmailSignupIntent
    data class PasswordChanged(val v: String) : EmailSignupIntent
    data class ConfirmChanged(val v: String) : EmailSignupIntent
    data object TogglePasswordVisibility : EmailSignupIntent
    data object ToggleConfirmVisibility : EmailSignupIntent
    data object BackTapped : EmailSignupIntent
    data object Submit : EmailSignupIntent
    data object LoginTapped : EmailSignupIntent
}
