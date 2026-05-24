package org.betech.fitnes.presentation.onboarding.pwdresetemail

sealed interface PwdResetEmailSideEffect {
    data object NavigateBack : PwdResetEmailSideEffect
    data object NavigateToForm : PwdResetEmailSideEffect
}
