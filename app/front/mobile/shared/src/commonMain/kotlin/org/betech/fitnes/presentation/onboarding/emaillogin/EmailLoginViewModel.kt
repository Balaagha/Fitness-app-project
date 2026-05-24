package org.betech.fitnes.presentation.onboarding.emaillogin

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.AuthRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Email Login VM (Pencil O8lWVO).
 *
 * Mock-only: calls [AuthRepository.signInWithEmail]; on success emits
 * AuthSignedIn(method="email") and navigates to Paywall stub.
 *
 * Re-entry guard: while [EmailLoginState.isSubmitting] is true, only
 * BackTapped is honoured (other intents are dropped).
 */
class EmailLoginViewModel(
    private val auth: AuthRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<EmailLoginState, EmailLoginSideEffect> {

    override val container =
        container<EmailLoginState, EmailLoginSideEffect>(EmailLoginState())

    fun onIntent(i: EmailLoginIntent) = intent {
        if (state.isSubmitting && i !is EmailLoginIntent.BackTapped) return@intent

        when (i) {
            is EmailLoginIntent.EmailChanged ->
                reduce { state.copy(email = i.v, errorMessage = null) }

            is EmailLoginIntent.PasswordChanged ->
                reduce { state.copy(password = i.v, errorMessage = null) }

            EmailLoginIntent.TogglePasswordVisibility ->
                reduce { state.copy(showPassword = !state.showPassword) }

            EmailLoginIntent.BackTapped ->
                postSideEffect(EmailLoginSideEffect.NavigateBack)

            EmailLoginIntent.ForgotPasswordTapped ->
                postSideEffect(EmailLoginSideEffect.NavigateToForgotPassword)

            EmailLoginIntent.SignupTapped ->
                postSideEffect(EmailLoginSideEffect.NavigateToSignup)

            EmailLoginIntent.Submit -> {
                if (!state.canSubmit) return@intent
                reduce { state.copy(isSubmitting = true, errorMessage = null) }
                val res = auth.signInWithEmail(state.email.trim(), state.password)
                if (res.isSuccess) {
                    analytics.track(AnalyticsEvent.AuthSignedIn(method = "email"))
                    postSideEffect(EmailLoginSideEffect.NavigateToPaywall)
                    reduce { state.copy(isSubmitting = false) }
                } else {
                    reduce {
                        state.copy(
                            isSubmitting = false,
                            errorMessage = res.exceptionOrNull()?.message ?: "auth_failed",
                        )
                    }
                }
            }
        }
    }
}
