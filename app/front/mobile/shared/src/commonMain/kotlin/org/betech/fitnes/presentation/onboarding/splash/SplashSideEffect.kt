package org.betech.fitnes.presentation.onboarding.splash

sealed interface SplashSideEffect {
    data object NavigateToLanguageSelect : SplashSideEffect
}
