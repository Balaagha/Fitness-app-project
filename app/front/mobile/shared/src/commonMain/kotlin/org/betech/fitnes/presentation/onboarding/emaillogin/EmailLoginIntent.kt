package org.betech.fitnes.presentation.onboarding.emaillogin

sealed interface EmailLoginIntent {
    data class EmailChanged(val v: String) : EmailLoginIntent
    data class PasswordChanged(val v: String) : EmailLoginIntent
    data object TogglePasswordVisibility : EmailLoginIntent
    data object BackTapped : EmailLoginIntent
    data object ForgotPasswordTapped : EmailLoginIntent
    data object SignupTapped : EmailLoginIntent
    data object Submit : EmailLoginIntent
}
