package org.betech.fitnes.presentation.onboarding.pwdresetemail

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Password Reset · Email VM (Pencil rzAPa).
 *
 * Mock-only — AuthRepository has no `requestPasswordReset` yet, so we simulate
 * an 800 ms network call and navigate forward to the form (token-entry) stub.
 * Real Supabase `auth.resetPasswordForEmail` wiring lands in a later iteration.
 *
 * Re-entry guard: while [PwdResetEmailState.isSubmitting] is true, only
 * BackTapped is honoured (other intents are dropped).
 */
class PwdResetEmailViewModel :
    ViewModel(),
    ContainerHost<PwdResetEmailState, PwdResetEmailSideEffect> {

    override val container =
        container<PwdResetEmailState, PwdResetEmailSideEffect>(PwdResetEmailState())

    fun onIntent(i: PwdResetEmailIntent) = intent {
        if (state.isSubmitting && i !is PwdResetEmailIntent.BackTapped) return@intent

        when (i) {
            is PwdResetEmailIntent.EmailChanged ->
                reduce { state.copy(email = i.v, errorMessage = null) }

            PwdResetEmailIntent.BackTapped ->
                postSideEffect(PwdResetEmailSideEffect.NavigateBack)

            PwdResetEmailIntent.Submit -> {
                if (!state.canSubmit) return@intent
                reduce { state.copy(isSubmitting = true, errorMessage = null) }
                delay(800)
                postSideEffect(PwdResetEmailSideEffect.NavigateToForm)
                reduce { state.copy(isSubmitting = false) }
            }
        }
    }
}
