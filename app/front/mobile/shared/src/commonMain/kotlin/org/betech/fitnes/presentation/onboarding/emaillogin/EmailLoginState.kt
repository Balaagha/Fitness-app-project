package org.betech.fitnes.presentation.onboarding.emaillogin

/**
 * Email Login state (Pencil O8lWVO).
 *
 * Mirrors EmailSignupState but lighter — no password rules; only "non-empty"
 * gate. CTA enabled when [emailValid] && password.isNotEmpty().
 */
data class EmailLoginState(
    val email: String = "",
    val password: String = "",
    val showPassword: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val emailValid: Boolean
        get() = email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))
    val canSubmit: Boolean
        get() = emailValid && password.isNotEmpty() && !isSubmitting
}
