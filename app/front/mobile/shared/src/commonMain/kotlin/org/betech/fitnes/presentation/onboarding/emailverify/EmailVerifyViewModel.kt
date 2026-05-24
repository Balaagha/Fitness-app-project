package org.betech.fitnes.presentation.onboarding.emailverify

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay
import kotlin.math.max
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Email Verify VM (Pencil zREhj).
 *
 * Mock-only: when 6 digits are entered, simulates a 1s verify call then emits
 * NavigateToPaywall. No real `AuthRepository.verifyEmail` exists yet — this
 * iteration intentionally stops at the simulation layer.
 *
 * Re-entry guard: while [EmailVerifyState.isSubmitting] is true, only
 * BackTapped / TickResend are honoured (other intents are dropped).
 */
class EmailVerifyViewModel :
    ViewModel(),
    ContainerHost<EmailVerifyState, EmailVerifySideEffect> {

    override val container =
        container<EmailVerifyState, EmailVerifySideEffect>(EmailVerifyState())

    fun onIntent(i: EmailVerifyIntent) = intent {
        if (state.isSubmitting &&
            i !is EmailVerifyIntent.BackTapped &&
            i !is EmailVerifyIntent.TickResend
        ) return@intent

        when (i) {
            is EmailVerifyIntent.CodeChanged -> {
                val sanitized = i.v.filter { it.isDigit() }.take(6)
                reduce { state.copy(code = sanitized, errorMessage = null) }
                if (sanitized.length == 6 && !state.isSubmitting) {
                    reduce { state.copy(isSubmitting = true) }
                    // Mock verify — repo method does not exist yet (Phase-2 backend wiring).
                    delay(1000)
                    postSideEffect(EmailVerifySideEffect.NavigateToPaywall)
                    reduce { state.copy(isSubmitting = false) }
                }
            }

            EmailVerifyIntent.TickResend -> {
                if (state.secondsLeft > 0) {
                    reduce { state.copy(secondsLeft = max(0, state.secondsLeft - 1)) }
                }
            }

            EmailVerifyIntent.ResendTapped -> {
                if (!state.canResend) return@intent
                reduce { state.copy(secondsLeft = 45, code = "") }
                postSideEffect(EmailVerifySideEffect.ResendSentToast)
            }

            EmailVerifyIntent.WrongEmailTapped ->
                postSideEffect(EmailVerifySideEffect.NavigateBack)

            EmailVerifyIntent.BackTapped ->
                postSideEffect(EmailVerifySideEffect.NavigateBack)
        }
    }
}
