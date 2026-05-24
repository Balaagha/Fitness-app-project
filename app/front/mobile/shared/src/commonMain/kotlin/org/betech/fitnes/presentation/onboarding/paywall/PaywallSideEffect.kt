package org.betech.fitnes.presentation.onboarding.paywall

/**
 * Paywall one-shot navigation/UX effects (Pencil ij7jR).
 *
 * `NavigateToHome` covers both flows — successful purchase AND X-close skip —
 * because the post-paywall destination is the same Home stub. The triggering
 * intent (Continue vs Close) is what decides the analytics event, not the SE.
 */
sealed interface PaywallSideEffect {
    data object NavigateToHome : PaywallSideEffect
    data object ShowRestoreToast : PaywallSideEffect
    data object ShowTermsToast : PaywallSideEffect
}
