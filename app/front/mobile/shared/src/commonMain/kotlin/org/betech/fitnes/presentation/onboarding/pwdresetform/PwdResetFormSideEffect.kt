package org.betech.fitnes.presentation.onboarding.pwdresetform

sealed interface PwdResetFormSideEffect {
    data object NavigateBack : PwdResetFormSideEffect
    data object NavigateToEmailLogin : PwdResetFormSideEffect
}
