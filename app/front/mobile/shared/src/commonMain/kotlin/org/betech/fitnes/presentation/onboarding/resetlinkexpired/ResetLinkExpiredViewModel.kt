package org.betech.fitnes.presentation.onboarding.resetlinkexpired

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/** V6 · Reset Link Expired VM (Pencil a3Vwh6). Visual-only snapshot — no repo deps. */
class ResetLinkExpiredViewModel :
    ViewModel(), ContainerHost<ResetLinkExpiredState, ResetLinkExpiredSideEffect> {

    override val container =
        container<ResetLinkExpiredState, ResetLinkExpiredSideEffect>(ResetLinkExpiredState)

    fun onIntent(i: ResetLinkExpiredIntent) = intent {
        when (i) {
            ResetLinkExpiredIntent.RequestNewTapped ->
                postSideEffect(ResetLinkExpiredSideEffect.NavigateToPwdReset)
            ResetLinkExpiredIntent.BackTapped ->
                postSideEffect(ResetLinkExpiredSideEffect.NavigateBack)
        }
    }
}
