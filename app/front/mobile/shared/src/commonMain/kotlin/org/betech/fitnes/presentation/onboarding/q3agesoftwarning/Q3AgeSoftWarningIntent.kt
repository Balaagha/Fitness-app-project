package org.betech.fitnes.presentation.onboarding.q3agesoftwarning

sealed interface Q3AgeSoftWarningIntent {
    data object BackTapped : Q3AgeSoftWarningIntent
    data object ContinueTapped : Q3AgeSoftWarningIntent
    data class AgeChanged(val v: Int) : Q3AgeSoftWarningIntent
}
