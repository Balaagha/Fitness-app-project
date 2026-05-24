package org.betech.fitnes.presentation.onboarding.authgate

sealed interface AuthGateIntent {
    data object BackTapped : AuthGateIntent
    data object AppleTapped : AuthGateIntent
    data object GoogleTapped : AuthGateIntent
    data object EmailTapped : AuthGateIntent
    data object SkipTapped : AuthGateIntent
    data object TermsTapped : AuthGateIntent
    data object PrivacyTapped : AuthGateIntent
}
