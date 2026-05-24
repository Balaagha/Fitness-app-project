package org.betech.fitnes.presentation.onboarding.signoutconfirm

/**
 * Signout Confirm screen state.
 *
 * - [isSigningOut] gates the destructive CTA during the mock signOut() round-trip.
 */
data class SignoutConfirmState(
    val isSigningOut: Boolean = false,
)
