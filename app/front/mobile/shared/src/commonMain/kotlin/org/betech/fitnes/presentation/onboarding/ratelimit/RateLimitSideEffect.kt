package org.betech.fitnes.presentation.onboarding.ratelimit

sealed interface RateLimitSideEffect {
    data object NavigateToPwdReset : RateLimitSideEffect
    data object Dismiss : RateLimitSideEffect
}
