package org.betech.fitnes.presentation.onboarding.todaysafeworkout

sealed interface TodaySafeWorkoutSideEffect {
    data object NavigateBack : TodaySafeWorkoutSideEffect
    data object NavigateToExerciseDetail : TodaySafeWorkoutSideEffect
}
