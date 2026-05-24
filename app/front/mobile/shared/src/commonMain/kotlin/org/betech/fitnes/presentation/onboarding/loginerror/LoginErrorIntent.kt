package org.betech.fitnes.presentation.onboarding.loginerror

sealed interface LoginErrorIntent {
    data class EmailChanged(val v: String) : LoginErrorIntent
    data class PasswordChanged(val v: String) : LoginErrorIntent
    data object TogglePasswordVisibility : LoginErrorIntent
    data object BackTapped : LoginErrorIntent
    data object ForgotPasswordTapped : LoginErrorIntent
    data object SubmitTapped : LoginErrorIntent
}
