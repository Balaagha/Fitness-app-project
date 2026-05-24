package org.betech.fitnes.presentation.onboarding.authgate

sealed interface AuthGateSideEffect {
    data object NavigateBack : AuthGateSideEffect
    data object NavigateToEmailSignup : AuthGateSideEffect
    data object NavigateToPaywall : AuthGateSideEffect
    /** [messageKey] is a stable key (see [LegalToastKey]) — composable resolves to locale string. */
    data class ShowToast(val messageKey: String) : AuthGateSideEffect
}

/** Stable toast keys decoupled from locale strings. */
object LegalToastKey {
    const val LEGAL_TODO = "legal_todo"
}
