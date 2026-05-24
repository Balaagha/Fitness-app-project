package org.betech.fitnes.presentation.onboarding.ratelimit

/** V3 · Rate Limit (Pencil IFSQ3). Static countdown for screenshot. */
data class RateLimitState(
    val minutesLeft: Int = 14,
    val secondsLeft: Int = 32,
)
