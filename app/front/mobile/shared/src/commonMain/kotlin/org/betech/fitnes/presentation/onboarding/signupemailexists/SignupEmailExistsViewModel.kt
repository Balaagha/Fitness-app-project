package org.betech.fitnes.presentation.onboarding.signupemailexists

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/** V2 · Signup Email Exists VM (Pencil cYfk5). Visual-only snapshot — no repo deps. */
class SignupEmailExistsViewModel :
    ViewModel(), ContainerHost<SignupEmailExistsState, SignupEmailExistsSideEffect> {

    override val container =
        container<SignupEmailExistsState, SignupEmailExistsSideEffect>(SignupEmailExistsState())

    fun onIntent(i: SignupEmailExistsIntent) = intent {
        when (i) {
            is SignupEmailExistsIntent.EmailChanged -> reduce { state.copy(email = i.v) }
            is SignupEmailExistsIntent.PasswordChanged -> reduce { state.copy(password = i.v) }
            is SignupEmailExistsIntent.ConfirmChanged -> reduce { state.copy(confirm = i.v) }
            SignupEmailExistsIntent.BackTapped ->
                postSideEffect(SignupEmailExistsSideEffect.NavigateBack)
            SignupEmailExistsIntent.GoLoginTapped ->
                postSideEffect(SignupEmailExistsSideEffect.NavigateToEmailLogin)
            SignupEmailExistsIntent.PrivacyTapped ->
                postSideEffect(SignupEmailExistsSideEffect.ShowPrivacyToast)
        }
    }
}
