package org.betech.fitnes.presentation.onboarding.pregnancyconfirm

sealed interface PregnancyConfirmSideEffect {
    data object NavigateBack : PregnancyConfirmSideEffect
    data object NavigateToTrimester : PregnancyConfirmSideEffect
}
