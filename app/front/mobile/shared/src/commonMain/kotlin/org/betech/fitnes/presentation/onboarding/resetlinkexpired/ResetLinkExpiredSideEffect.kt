package org.betech.fitnes.presentation.onboarding.resetlinkexpired

sealed interface ResetLinkExpiredSideEffect {
    data object NavigateToPwdReset : ResetLinkExpiredSideEffect
    data object NavigateBack : ResetLinkExpiredSideEffect
}
