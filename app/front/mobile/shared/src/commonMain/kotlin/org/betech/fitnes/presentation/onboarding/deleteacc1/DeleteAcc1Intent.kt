package org.betech.fitnes.presentation.onboarding.deleteacc1

/** User intents for Delete Account · Step 1 (Pencil e74FR). */
sealed interface DeleteAcc1Intent {
    data object BackTapped : DeleteAcc1Intent
    data object CancelTapped : DeleteAcc1Intent
    data object ContinueTapped : DeleteAcc1Intent
}
