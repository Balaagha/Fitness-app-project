package org.betech.fitnes.presentation.onboarding.welcome

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Welcome screen VM — Orbit MVI.
 *
 * Pure routing + a small variant reducer. Splash already emitted
 * `OnboardingStarted`, so no duplicate analytics event is fired here.
 *
 * `Rotate` cycles through all 5 [WelcomeVariant] entries; `JumpTo` lets dev
 * deep-links land directly on a specific variant.
 */
class WelcomeViewModel(
    @Suppress("unused") private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<WelcomeState, WelcomeSideEffect> {

    override val container = container<WelcomeState, WelcomeSideEffect>(WelcomeState())

    fun onIntent(intent: WelcomeIntent) = intent {
        when (intent) {
            WelcomeIntent.StartTapped ->
                postSideEffect(WelcomeSideEffect.NavigateToQ1Goal)
            WelcomeIntent.HaveAccountTapped ->
                postSideEffect(WelcomeSideEffect.NavigateToLogin)
            WelcomeIntent.ChangeLanguageTapped ->
                postSideEffect(WelcomeSideEffect.NavigateToLanguageSelect)
            WelcomeIntent.Rotate -> {
                val all = WelcomeVariant.entries
                val nextOrdinal = (state.activeVariant.ordinal + 1) % all.size
                reduce { state.copy(activeVariant = all[nextOrdinal]) }
            }
            is WelcomeIntent.JumpTo ->
                reduce { state.copy(activeVariant = intent.variant) }
        }
    }
}
