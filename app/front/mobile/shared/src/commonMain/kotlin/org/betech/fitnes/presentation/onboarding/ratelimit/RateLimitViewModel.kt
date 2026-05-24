package org.betech.fitnes.presentation.onboarding.ratelimit

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/** V3 · Rate Limit VM (Pencil IFSQ3). Visual-only — no repo deps, static countdown. */
class RateLimitViewModel :
    ViewModel(), ContainerHost<RateLimitState, RateLimitSideEffect> {

    override val container = container<RateLimitState, RateLimitSideEffect>(RateLimitState())

    fun onIntent(i: RateLimitIntent) = intent {
        when (i) {
            RateLimitIntent.ResetTapped ->
                postSideEffect(RateLimitSideEffect.NavigateToPwdReset)
            RateLimitIntent.DismissTapped ->
                postSideEffect(RateLimitSideEffect.Dismiss)
        }
    }
}
