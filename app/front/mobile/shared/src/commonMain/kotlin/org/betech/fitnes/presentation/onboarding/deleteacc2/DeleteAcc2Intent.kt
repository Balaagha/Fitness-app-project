package org.betech.fitnes.presentation.onboarding.deleteacc2

/** User intents for Delete Account · Step 2 (Pencil GauGs). */
sealed interface DeleteAcc2Intent {
    data class InputChanged(val value: String) : DeleteAcc2Intent
    data object BackTapped : DeleteAcc2Intent
    data object ConfirmTapped : DeleteAcc2Intent
}
