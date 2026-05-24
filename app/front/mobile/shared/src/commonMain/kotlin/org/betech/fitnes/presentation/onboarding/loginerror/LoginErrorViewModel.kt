package org.betech.fitnes.presentation.onboarding.loginerror

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * V1 · Login Error VM (Pencil u27ve).
 *
 * Visual-only error snapshot for QA review — no repo deps. Mirrors
 * EmailLogin layout with a forced-invalid password state.
 */
class LoginErrorViewModel :
    ViewModel(), ContainerHost<LoginErrorState, LoginErrorSideEffect> {

    override val container = container<LoginErrorState, LoginErrorSideEffect>(LoginErrorState())

    fun onIntent(i: LoginErrorIntent) = intent {
        when (i) {
            is LoginErrorIntent.EmailChanged -> reduce { state.copy(email = i.v) }
            is LoginErrorIntent.PasswordChanged -> reduce { state.copy(password = i.v) }
            LoginErrorIntent.TogglePasswordVisibility ->
                reduce { state.copy(showPassword = !state.showPassword) }
            LoginErrorIntent.BackTapped ->
                postSideEffect(LoginErrorSideEffect.NavigateBack)
            LoginErrorIntent.ForgotPasswordTapped ->
                postSideEffect(LoginErrorSideEffect.NavigateToForgotPassword)
            LoginErrorIntent.SubmitTapped -> Unit // visual snapshot — no submit
        }
    }
}
