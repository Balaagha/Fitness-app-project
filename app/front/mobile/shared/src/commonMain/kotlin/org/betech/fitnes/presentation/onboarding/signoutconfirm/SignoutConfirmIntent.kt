package org.betech.fitnes.presentation.onboarding.signoutconfirm

/**
 * User-driven intents for the Signout Confirm screen (Pencil SCKUA · "23").
 */
sealed interface SignoutConfirmIntent {
    data object ConfirmTapped : SignoutConfirmIntent
    data object CancelTapped : SignoutConfirmIntent
    data object BackTapped : SignoutConfirmIntent
}
