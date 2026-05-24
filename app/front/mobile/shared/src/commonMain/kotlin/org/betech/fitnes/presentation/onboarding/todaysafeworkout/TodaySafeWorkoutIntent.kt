package org.betech.fitnes.presentation.onboarding.todaysafeworkout

sealed interface TodaySafeWorkoutIntent {
    data object BackTapped : TodaySafeWorkoutIntent
    data object StartWorkoutTapped : TodaySafeWorkoutIntent
    data class ExerciseTapped(val index: Int) : TodaySafeWorkoutIntent
}
