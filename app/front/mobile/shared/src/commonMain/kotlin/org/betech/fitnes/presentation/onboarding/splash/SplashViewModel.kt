package org.betech.fitnes.presentation.onboarding.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Splash screen ViewModel — fires onboarding_started analytics, waits ~1.4s
 * for the brand mark to settle, then asks the UI to navigate forward.
 *
 * No user input. Self-driven by [SplashIntent.Start] (dispatched once on mount).
 */
class SplashViewModel(
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<SplashState, SplashSideEffect> {

    override val container = container<SplashState, SplashSideEffect>(SplashState())

    fun onIntent(intent: SplashIntent) = when (intent) {
        SplashIntent.Start -> start()
    }

    private fun start() = intent {
        analytics.track(AnalyticsEvent.OnboardingStarted)
        delay(SPLASH_DURATION_MS)
        reduce { state.copy(isReady = true) }
        postSideEffect(SplashSideEffect.NavigateToLanguageSelect)
    }

    private companion object {
        const val SPLASH_DURATION_MS = 1_400L
    }
}
