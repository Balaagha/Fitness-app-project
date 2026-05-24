package org.betech.fitnes.presentation.onboarding.signoutconfirm

import androidx.lifecycle.ViewModel
import org.betech.fitnes.domain.repository.AuthRepository
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Signout Confirm VM (Pencil SCKUA · "23 · Çıxış Təsdiq") — Orbit MVI.
 *
 * Behaviour contract:
 *  - `ConfirmTapped`  → call mock `authRepo.signOut()`, then full session reset
 *                       via `NavigateToLanguageSelect`.
 *  - `CancelTapped` / `BackTapped` → `NavigateBack` (pop).
 *
 * Re-entry guard: while `isSigningOut` is true, every intent is ignored so a
 * double-tap on Confirm cannot stack signOut() calls.
 *
 * NOTE: no `AuthSignedOut` analytics variant exists in `AnalyticsEvent` yet;
 * emission is intentionally skipped until that variant lands.
 */
class SignoutConfirmViewModel(
    private val authRepo: AuthRepository,
) : ViewModel(), ContainerHost<SignoutConfirmState, SignoutConfirmSideEffect> {

    override val container =
        container<SignoutConfirmState, SignoutConfirmSideEffect>(SignoutConfirmState())

    fun onIntent(i: SignoutConfirmIntent) = intent {
        if (state.isSigningOut) return@intent

        when (i) {
            SignoutConfirmIntent.ConfirmTapped -> {
                reduce { state.copy(isSigningOut = true) }
                runCatching { authRepo.signOut() }
                reduce { state.copy(isSigningOut = false) }
                postSideEffect(SignoutConfirmSideEffect.NavigateToLanguageSelect)
            }
            SignoutConfirmIntent.CancelTapped,
            SignoutConfirmIntent.BackTapped ->
                postSideEffect(SignoutConfirmSideEffect.NavigateBack)
        }
    }
}
