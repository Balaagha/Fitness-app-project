package org.betech.fitnes.presentation.onboarding.deleteacc2

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Delete Account · Step 2 VM (Pencil GauGs) — Orbit MVI.
 *
 * MVP scope: simulates the cascade-delete call with an 800ms delay, then
 * routes back to LanguageSelect. The real wire-up (Supabase Edge Function
 * `account_delete_cascade` + Storage purge + 30-day soft-delete grace per
 * project-context.md §10.3) lands when the AuthRepository gains the
 * `deleteAccount()` operation — keep the VM signature stable so swap-in is
 * a one-line repo injection.
 *
 * Guards:
 *  - Confirm is a no-op unless [DeleteAcc2State.matches] is true (the UI
 *    already disables the button; the guard here is defence-in-depth against
 *    accessibility-driven double activation).
 *  - [DeleteAcc2State.isDeleting] prevents re-entry while the mock call runs.
 */
class DeleteAcc2ViewModel :
    ViewModel(),
    ContainerHost<DeleteAcc2State, DeleteAcc2SideEffect> {

    override val container =
        container<DeleteAcc2State, DeleteAcc2SideEffect>(DeleteAcc2State())

    fun onIntent(i: DeleteAcc2Intent) = intent {
        when (i) {
            is DeleteAcc2Intent.InputChanged -> {
                if (!state.isDeleting) reduce { state.copy(input = i.value) }
            }
            DeleteAcc2Intent.BackTapped ->
                postSideEffect(DeleteAcc2SideEffect.NavigateBack)
            DeleteAcc2Intent.ConfirmTapped -> {
                if (!state.matches || state.isDeleting) return@intent
                reduce { state.copy(isDeleting = true) }
                // TODO: replace with AuthRepository.deleteAccount() once available.
                delay(800)
                postSideEffect(DeleteAcc2SideEffect.NavigateToLanguageSelect)
            }
        }
    }
}
