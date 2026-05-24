package org.betech.fitnes.presentation.onboarding.signoutconfirm

/**
 * Signout Confirm one-shot navigation effects (Pencil SCKUA).
 *
 * - [NavigateToLanguageSelect] is the full session-reset destination after a
 *   successful signOut() round-trip.
 * - [NavigateBack] covers both the "İmtina et" CTA and the back chevron.
 */
sealed interface SignoutConfirmSideEffect {
    data object NavigateToLanguageSelect : SignoutConfirmSideEffect
    data object NavigateBack : SignoutConfirmSideEffect
}
