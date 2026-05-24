package org.betech.fitnes.presentation.onboarding.offlinebanner

sealed interface OfflineBannerSideEffect {
    data object NavigateBack : OfflineBannerSideEffect
    data object Refresh : OfflineBannerSideEffect
}
