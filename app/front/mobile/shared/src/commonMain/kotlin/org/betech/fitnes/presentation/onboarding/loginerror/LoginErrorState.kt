package org.betech.fitnes.presentation.onboarding.loginerror

/** V1 — Login Error (Pencil u27ve). QA visual snapshot only. */
data class LoginErrorState(
    val email: String = "ali@example.com",
    val password: String = "wrong-pw",
    val showPassword: Boolean = false,
)
