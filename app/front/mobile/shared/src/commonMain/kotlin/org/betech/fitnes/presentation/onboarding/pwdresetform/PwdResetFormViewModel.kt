package org.betech.fitnes.presentation.onboarding.pwdresetform

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Password Reset · Form VM (Pencil vA9Tb).
 *
 * Mock-only: simulates an async `auth.updateUser({ password })` call with a
 * fixed 800ms delay, then navigates to EmailLogin so the user signs in with
 * the new password. Real Supabase wiring lands when AuthRepository gains a
 * `confirmPasswordReset(token, newPassword)` method.
 *
 * Re-entry guard: while [PwdResetFormState.isSubmitting] only BackTapped is
 * honoured (other intents are dropped).
 */
class PwdResetFormViewModel :
    ViewModel(),
    ContainerHost<PwdResetFormState, PwdResetFormSideEffect> {

    override val container =
        container<PwdResetFormState, PwdResetFormSideEffect>(PwdResetFormState())

    fun onIntent(i: PwdResetFormIntent) = intent {
        if (state.isSubmitting && i !is PwdResetFormIntent.BackTapped) return@intent

        when (i) {
            is PwdResetFormIntent.PasswordChanged ->
                reduce { state.copy(password = i.v, errorMessage = null) }

            is PwdResetFormIntent.ConfirmChanged ->
                reduce { state.copy(confirm = i.v, errorMessage = null) }

            PwdResetFormIntent.TogglePasswordVisibility ->
                reduce { state.copy(showPassword = !state.showPassword) }

            PwdResetFormIntent.ToggleConfirmVisibility ->
                reduce { state.copy(showConfirm = !state.showConfirm) }

            PwdResetFormIntent.BackTapped ->
                postSideEffect(PwdResetFormSideEffect.NavigateBack)

            PwdResetFormIntent.Submit -> {
                if (!state.canSubmit) return@intent
                reduce { state.copy(isSubmitting = true, errorMessage = null) }
                delay(800) // mock backend round-trip
                postSideEffect(PwdResetFormSideEffect.NavigateToEmailLogin)
                reduce { state.copy(isSubmitting = false) }
            }
        }
    }
}
