package org.betech.fitnes.presentation.onboarding.deleteacc1

/**
 * One-shot navigation effects for Delete Account · Step 1 (Pencil e74FR).
 *
 * - [NavigateBack] handles both the chevron and the "İmtina et" CTA — they
 *   share the same intent: pop the destructive flow without state changes.
 * - [NavigateToDeleteAcc2] routes to the second confirmation step
 *   (Pencil GauGs · typed-confirmation gate).
 */
sealed interface DeleteAcc1SideEffect {
    data object NavigateBack : DeleteAcc1SideEffect
    data object NavigateToDeleteAcc2 : DeleteAcc1SideEffect
}
