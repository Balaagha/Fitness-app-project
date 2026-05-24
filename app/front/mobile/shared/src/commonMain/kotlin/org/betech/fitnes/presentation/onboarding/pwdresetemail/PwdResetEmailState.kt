package org.betech.fitnes.presentation.onboarding.pwdresetemail

/**
 * Password Reset · Email state (Pencil rzAPa).
 *
 * Single-field form: e-mail with simple regex validity gate. CTA enabled
 * when [emailValid] && !isSubmitting.
 */
data class PwdResetEmailState(
    val email: String = "",
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val emailValid: Boolean
        get() = email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))
    val canSubmit: Boolean
        get() = emailValid && !isSubmitting
}
