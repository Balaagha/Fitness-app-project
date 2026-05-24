package org.betech.fitnes.presentation.onboarding.pwdresetform

/**
 * Password Reset · Form state (Pencil vA9Tb).
 *
 * Same derived-validation pattern as EmailSignupState — no email field; the
 * recovery token is conceptually carried by the deep-link the user followed.
 */
data class PwdResetFormState(
    val password: String = "",
    val confirm: String = "",
    val showPassword: Boolean = false,
    val showConfirm: Boolean = false,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val pwLength: Boolean get() = password.length >= 8
    val pwUpper: Boolean get() = password.any { it.isUpperCase() }
    val pwDigit: Boolean get() = password.any { it.isDigit() }
    val pwSpecial: Boolean get() = password.any { !it.isLetterOrDigit() }
    val pwValid: Boolean get() = pwLength && pwUpper && pwDigit && pwSpecial
    val confirmMatches: Boolean get() = confirm.isNotEmpty() && confirm == password
    val canSubmit: Boolean
        get() = pwValid && confirmMatches && !isSubmitting
}
