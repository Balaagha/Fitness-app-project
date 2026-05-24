package org.betech.fitnes.presentation.onboarding.welcome

sealed interface WelcomeIntent {
    data object StartTapped : WelcomeIntent
    data object HaveAccountTapped : WelcomeIntent
    data object ChangeLanguageTapped : WelcomeIntent
    /** Triggered by LaunchedEffect timer to cycle to the next variant. */
    data object Rotate : WelcomeIntent
    /** Dev deep-link or programmatic jump to a specific variant. */
    data class JumpTo(val variant: WelcomeVariant) : WelcomeIntent
}
