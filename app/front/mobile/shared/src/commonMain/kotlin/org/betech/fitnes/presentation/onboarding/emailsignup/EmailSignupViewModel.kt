package org.betech.fitnes.presentation.onboarding.emailsignup

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.model.AnalyticsEvent
import org.betech.fitnes.domain.repository.AnalyticsRepository
import org.betech.fitnes.domain.repository.AuthRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Email Signup VM (Pencil ZJFFO).
 *
 * Mock-only: calls [AuthRepository.signUpWithEmail]; on success emits
 * AuthSignedIn(method="email") and navigates to EmailVerify stub.
 *
 * Re-entry guard: while [EmailSignupState.isSubmitting] is true, only
 * BackTapped is honoured (other intents are dropped).
 */
class EmailSignupViewModel(
    private val auth: AuthRepository,
    private val analytics: AnalyticsRepository,
) : ViewModel(), ContainerHost<EmailSignupState, EmailSignupSideEffect> {

    override val container =
        container<EmailSignupState, EmailSignupSideEffect>(EmailSignupState())

    fun onIntent(i: EmailSignupIntent) = intent {
        if (state.isSubmitting && i !is EmailSignupIntent.BackTapped) return@intent

        when (i) {
            is EmailSignupIntent.EmailChanged ->
                reduce { state.copy(email = i.v, errorMessage = null) }

            is EmailSignupIntent.PasswordChanged ->
                reduce { state.copy(password = i.v, errorMessage = null) }

            is EmailSignupIntent.ConfirmChanged ->
                reduce { state.copy(confirm = i.v, errorMessage = null) }

            EmailSignupIntent.TogglePasswordVisibility ->
                reduce { state.copy(showPassword = !state.showPassword) }

            EmailSignupIntent.ToggleConfirmVisibility ->
                reduce { state.copy(showConfirm = !state.showConfirm) }

            EmailSignupIntent.BackTapped ->
                postSideEffect(EmailSignupSideEffect.NavigateBack)

            EmailSignupIntent.LoginTapped ->
                postSideEffect(EmailSignupSideEffect.NavigateToEmailLogin)

            EmailSignupIntent.Submit -> {
                if (!state.canSubmit) return@intent
                reduce { state.copy(isSubmitting = true, errorMessage = null) }
                val res = auth.signUpWithEmail(state.email.trim(), state.password)
                if (res.isSuccess) {
                    analytics.track(AnalyticsEvent.AuthSignedIn(method = "email"))
                    postSideEffect(EmailSignupSideEffect.NavigateToEmailVerify)
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
