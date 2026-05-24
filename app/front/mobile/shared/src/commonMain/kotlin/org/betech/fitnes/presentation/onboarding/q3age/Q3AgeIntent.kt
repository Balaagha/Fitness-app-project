package org.betech.fitnes.presentation.onboarding.q3age

sealed interface Q3AgeIntent {
    data object Decrement : Q3AgeIntent
    data object Increment : Q3AgeIntent
    /** From slider or dialog confirmation; will be clamped to [Q3AgeState.MIN_AGE]..[Q3AgeState.MAX_AGE]. */
    data class SetAge(val value: Int) : Q3AgeIntent
    data object OpenDialog : Q3AgeIntent
    data object CloseDialog : Q3AgeIntent
    data object BackTapped : Q3AgeIntent
    data object Confirm : Q3AgeIntent
}
