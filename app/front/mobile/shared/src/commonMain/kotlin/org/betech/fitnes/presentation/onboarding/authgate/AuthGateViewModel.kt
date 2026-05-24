package org.betech.fitnes.presentation.onboarding.authgate

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.AuthRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * AuthGate VM (Pencil K1n7u5).
 *
 * Mock-only: hits [AuthRepository.signInWithApple] / [signInWithGoogle] with
 * "MOCK_TOKEN" placeholder; MockRemoteSource resolves after a scripted delay
 * so the UI gets a visible loading state.
 *
 * Skip path emits [AnalyticsEvent.PaywallShown] (variant="guest") then
 * navigates to paywall — no UserProfile.guestMode flag exists yet, the
 * routing is the only signal.
 */
class AuthGateViewModel(
    private val auth: AuthRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<AuthGateState, AuthGateSideEffect> {

    override val container =
        container<AuthGateState, AuthGateSideEffect>(AuthGateState())

    fun onIntent(i: AuthGateIntent) = intent {
        // Block re-entry while a method is mid-flight (back is always allowed).
        if (state.inFlight != AuthInFlight.NONE && i !is AuthGateIntent.BackTapped) return@intent

        when (i) {
            AuthGateIntent.BackTapped ->
                postSideEffect(AuthGateSideEffect.NavigateBack)

            AuthGateIntent.AppleTapped -> {
                reduce { state.copy(inFlight = AuthInFlight.APPLE, errorMessage = null) }
                val res = auth.signInWithApple("MOCK_TOKEN")
                if (res.isSuccess) {
                    analytics.track(AnalyticsEvent.AuthSignedIn(method = "apple"))
                    postSideEffect(AuthGateSideEffect.NavigateToPaywall)
                    reduce { state.copy(inFlight = AuthInFlight.NONE) }
                } else {
                    reduce {
                        state.copy(
                            inFlight = AuthInFlight.NONE,
                            errorMessage = res.exceptionOrNull()?.message ?: "auth_failed",
                        )
                    }
                }
            }

            AuthGateIntent.GoogleTapped -> {
                reduce { state.copy(inFlight = AuthInFlight.GOOGLE, errorMessage = null) }
                val res = auth.signInWithGoogle("MOCK_TOKEN")
                if (res.isSuccess) {
                    analytics.track(AnalyticsEvent.AuthSignedIn(method = "google"))
                    postSideEffect(AuthGateSideEffect.NavigateToPaywall)
                    reduce { state.copy(inFlight = AuthInFlight.NONE) }
                } else {
                    reduce {
                        state.copy(
                            inFlight = AuthInFlight.NONE,
                            errorMessage = res.exceptionOrNull()?.message ?: "auth_failed",
                        )
                    }
                }
            }

            AuthGateIntent.EmailTapped -> {
                reduce { state.copy(inFlight = AuthInFlight.EMAIL, errorMessage = null) }
                postSideEffect(AuthGateSideEffect.NavigateToEmailSignup)
                reduce { state.copy(inFlight = AuthInFlight.NONE) }
            }

            AuthGateIntent.SkipTapped -> {
                reduce { state.copy(inFlight = AuthInFlight.SKIP, errorMessage = null) }
                analytics.track(AnalyticsEvent.PaywallShown(variant = "guest"))
                postSideEffect(AuthGateSideEffect.NavigateToPaywall)
                reduce { state.copy(inFlight = AuthInFlight.NONE) }
            }

            AuthGateIntent.TermsTapped,
            AuthGateIntent.PrivacyTapped ->
                postSideEffect(AuthGateSideEffect.ShowToast(LegalToastKey.LEGAL_TODO))
        }
    }
}
