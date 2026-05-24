package org.betech.fitnes.presentation.onboarding.pwdresetemail

sealed interface PwdResetEmailIntent {
    data class EmailChanged(val v: String) : PwdResetEmailIntent
    data object BackTapped : PwdResetEmailIntent
    data object Submit : PwdResetEmailIntent
}
