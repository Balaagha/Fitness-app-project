package org.betech.fitnes.presentation.onboarding.emailverify

/**
 * Email Verify state (Pencil zREhj).
 *
 * `code` is the raw 0-6 digit string typed by the user; the visual six-cell
 * row derives directly from it (cell `i` shows `code.getOrNull(i)`).
 */
data class EmailVerifyState(
    val email: String = "ad@nümunə.az",
    val code: String = "",
    val secondsLeft: Int = 45,
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
) {
    val canResend: Boolean get() = secondsLeft == 0
    val isComplete: Boolean get() = code.length == 6
}
