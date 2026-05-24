package org.betech.fitnes.presentation.onboarding.pwdresetform

sealed interface PwdResetFormIntent {
    data class PasswordChanged(val v: String) : PwdResetFormIntent
    data class ConfirmChanged(val v: String) : PwdResetFormIntent
    data object TogglePasswordVisibility : PwdResetFormIntent
    data object ToggleConfirmVisibility : PwdResetFormIntent
    data object BackTapped : PwdResetFormIntent
    data object Submit : PwdResetFormIntent
}
