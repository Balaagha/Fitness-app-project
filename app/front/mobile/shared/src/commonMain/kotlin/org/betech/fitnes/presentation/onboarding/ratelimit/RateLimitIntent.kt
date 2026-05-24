package org.betech.fitnes.presentation.onboarding.ratelimit

sealed interface RateLimitIntent {
    data object ResetTapped : RateLimitIntent
    data object DismissTapped : RateLimitIntent
}
