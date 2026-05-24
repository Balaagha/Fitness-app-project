package org.betech.fitnes.presentation.onboarding.splash

sealed interface SplashIntent {
    /** Fired once when the screen first composes. Kicks the auto-advance timer. */
    data object Start : SplashIntent
}
