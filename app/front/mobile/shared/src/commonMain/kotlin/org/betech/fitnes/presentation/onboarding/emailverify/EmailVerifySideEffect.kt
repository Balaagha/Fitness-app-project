package org.betech.fitnes.presentation.onboarding.emailverify

sealed interface EmailVerifySideEffect {
    data object NavigateBack : EmailVerifySideEffect
    data object NavigateToPaywall : EmailVerifySideEffect
    /**
     * No payload — the composable resolves the localised "code sent" copy
     * via [org.betech.fitnes.localization.Strings.emailVerifyResendToast].
     * Keeping locale lookup at the call-site mirrors AuthGate's LegalToastKey
     * pattern (no raw strings cross the VM boundary).
     */
    data object ResendSentToast : EmailVerifySideEffect
}
