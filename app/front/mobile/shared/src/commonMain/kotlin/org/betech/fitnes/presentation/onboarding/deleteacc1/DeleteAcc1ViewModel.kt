package org.betech.fitnes.presentation.onboarding.deleteacc1

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Delete Account · Step 1 VM (Pencil e74FR) — Orbit MVI.
 *
 * Reassurance/disclosure step only — NO repository calls here. The actual
 * cascade delete + 30-day soft-archive trigger lives in step 2 (Pencil GauGs)
 * after typed confirmation. This step's sole job is informed-consent UX:
 * show what will be archived and offer an explicit "İmtina et" exit.
 */
class DeleteAcc1ViewModel :
    ViewModel(),
    ContainerHost<DeleteAcc1State, DeleteAcc1SideEffect> {

    override val container =
        container<DeleteAcc1State, DeleteAcc1SideEffect>(DeleteAcc1State())

    fun onIntent(i: DeleteAcc1Intent) = intent {
        when (i) {
            DeleteAcc1Intent.BackTapped,
            DeleteAcc1Intent.CancelTapped ->
                postSideEffect(DeleteAcc1SideEffect.NavigateBack)
            DeleteAcc1Intent.ContinueTapped ->
                postSideEffect(DeleteAcc1SideEffect.NavigateToDeleteAcc2)
        }
    }
}
