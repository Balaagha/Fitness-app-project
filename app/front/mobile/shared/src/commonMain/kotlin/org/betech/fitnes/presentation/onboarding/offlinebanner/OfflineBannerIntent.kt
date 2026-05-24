package org.betech.fitnes.presentation.onboarding.offlinebanner

sealed interface OfflineBannerIntent {
    data class EmailChanged(val v: String) : OfflineBannerIntent
    data class PasswordChanged(val v: String) : OfflineBannerIntent
    data object BackTapped : OfflineBannerIntent
    data object RefreshTapped : OfflineBannerIntent
}
