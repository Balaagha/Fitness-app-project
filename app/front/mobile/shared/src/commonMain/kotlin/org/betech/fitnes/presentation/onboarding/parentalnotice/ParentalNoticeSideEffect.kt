package org.betech.fitnes.presentation.onboarding.parentalnotice

sealed interface ParentalNoticeSideEffect {
    data object NavigateBack : ParentalNoticeSideEffect
    data object NavigateForward : ParentalNoticeSideEffect
    data object OpenPrivacySheet : ParentalNoticeSideEffect
    data object OpenTermsSheet : ParentalNoticeSideEffect
}
