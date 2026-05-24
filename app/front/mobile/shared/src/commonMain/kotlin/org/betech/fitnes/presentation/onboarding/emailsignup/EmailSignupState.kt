package org.betech.fitnes.presentation.onboarding.emailsignup

/**
 * Email Signup state (Pencil ZJFFO).
 *
 * Pure derived validation — VM does not have to recompute on every keystroke;
 * Compose recomposition reads the derived gets on demand.
 */
data class EmailSignupState(
    val email: String = "",
    val password: String = "",
    val confirm: String = "",
    val showPassword: Boolean = false,
    val showConfirm: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val emailValid: Boolean
        get() = email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"))
    val pwLength: Boolean get() = password.length >= 8
    val pwUpper: Boolean get() = password.any { it.isUpperCase() }
    val pwDigit: Boolean get() = password.any { it.isDigit() }
    val pwSpecial: Boolean get() = password.any { !it.isLetterOrDigit() }
    val pwValid: Boolean get() = pwLength && pwUpper && pwDigit && pwSpecial
    val confirmMatches: Boolean get() = confirm.isNotEmpty() && confirm == password
    val canSubmit: Boolean
        get() = emailValid && pwValid && confirmMatches && !isSubmitting
}
