package org.betech.fitnes.presentation.onboarding.parentalnotice

sealed interface ParentalNoticeIntent {
    data object BackTapped : ParentalNoticeIntent
    data class ConsentToggled(val checked: Boolean) : ParentalNoticeIntent
    data object PrivacyChipTapped : ParentalNoticeIntent
    data object TermsChipTapped : ParentalNoticeIntent
    data object ContinueTapped : ParentalNoticeIntent
}
