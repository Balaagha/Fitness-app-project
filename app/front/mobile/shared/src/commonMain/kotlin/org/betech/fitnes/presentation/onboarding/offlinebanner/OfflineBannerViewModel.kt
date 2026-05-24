package org.betech.fitnes.presentation.onboarding.offlinebanner

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/** V4 · Offline Banner VM (Pencil K2TtZa). Visual-only snapshot — no repo deps. */
class OfflineBannerViewModel :
    ViewModel(), ContainerHost<OfflineBannerState, OfflineBannerSideEffect> {

    override val container =
        container<OfflineBannerState, OfflineBannerSideEffect>(OfflineBannerState())

    fun onIntent(i: OfflineBannerIntent) = intent {
        when (i) {
            is OfflineBannerIntent.EmailChanged -> reduce { state.copy(email = i.v) }
            is OfflineBannerIntent.PasswordChanged -> reduce { state.copy(password = i.v) }
            OfflineBannerIntent.BackTapped ->
                postSideEffect(OfflineBannerSideEffect.NavigateBack)
            OfflineBannerIntent.RefreshTapped ->
                postSideEffect(OfflineBannerSideEffect.Refresh)
        }
    }
}
