package org.betech.fitnes.presentation.onboarding.splash

/**
 * Splash screen state. The screen is non-interactive; `isReady` flips true
 * after the auto-advance timer elapses (used for tests / animation hooks).
 */
data class SplashState(
    val isReady: Boolean = false,
)
