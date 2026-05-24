package org.betech.fitnes.presentation.onboarding.authgate

/** Which auth method (if any) is mid-flight. Drives per-button spinner + disables. */
enum class AuthInFlight { NONE, APPLE, GOOGLE, EMAIL, SKIP }

data class AuthGateState(
    val inFlight: AuthInFlight = AuthInFlight.NONE,
    val errorMessage: String? = null,
)
