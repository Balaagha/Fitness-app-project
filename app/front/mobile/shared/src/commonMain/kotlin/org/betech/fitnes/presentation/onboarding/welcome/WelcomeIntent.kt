package org.betech.fitnes.presentation.onboarding.welcome

sealed interface WelcomeIntent {
    data class PageChanged(val index: Int) : WelcomeIntent
    data object SkipTapped : WelcomeIntent
    data object StartTapped : WelcomeIntent
    data object HaveAccountTapped : WelcomeIntent
    data object ChangeLanguageTapped : WelcomeIntent
}
