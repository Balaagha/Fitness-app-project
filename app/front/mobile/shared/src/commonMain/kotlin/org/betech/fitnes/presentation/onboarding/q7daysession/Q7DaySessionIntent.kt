package org.betech.fitnes.presentation.onboarding.q7daysession

sealed interface Q7DaySessionIntent {
    data class SetDays(val days: Int) : Q7DaySessionIntent
    data class SetMinutes(val minutes: Int) : Q7DaySessionIntent
    data object BackTapped : Q7DaySessionIntent
    data object Confirm : Q7DaySessionIntent
}
