package org.betech.fitnes.presentation.onboarding.emailverify

sealed interface EmailVerifyIntent {
    data class CodeChanged(val v: String) : EmailVerifyIntent
    data object TickResend : EmailVerifyIntent
    data object ResendTapped : EmailVerifyIntent
    data object WrongEmailTapped : EmailVerifyIntent
    data object BackTapped : EmailVerifyIntent
}
