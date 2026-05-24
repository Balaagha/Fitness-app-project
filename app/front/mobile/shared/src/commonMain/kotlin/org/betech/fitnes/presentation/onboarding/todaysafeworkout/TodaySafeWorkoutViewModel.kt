package org.betech.fitnes.presentation.onboarding.todaysafeworkout

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Today's Safe Workout VM (Orbit MVI · Pencil QHsnW).
 *
 * NO repository dependencies — session payload is a curated static template
 * baked into the UI layer. NO AI plan/exercise generation trigger ever
 * (CLAUDE.md hard-stop: pregnancy_postpartum=true → curated static template).
 *
 * Both the CTA and per-exercise row tap route to ExerciseDetailPregScreen
 * (Pencil YZ38M) — currently a stub.
 */
class TodaySafeWorkoutViewModel :
    ViewModel(),
    ContainerHost<TodaySafeWorkoutState, TodaySafeWorkoutSideEffect> {

    override val container =
        container<TodaySafeWorkoutState, TodaySafeWorkoutSideEffect>(TodaySafeWorkoutState)

    fun onIntent(i: TodaySafeWorkoutIntent) = intent {
        when (i) {
            TodaySafeWorkoutIntent.BackTapped ->
                postSideEffect(TodaySafeWorkoutSideEffect.NavigateBack)
            TodaySafeWorkoutIntent.StartWorkoutTapped ->
                postSideEffect(TodaySafeWorkoutSideEffect.NavigateToExerciseDetail)
            is TodaySafeWorkoutIntent.ExerciseTapped ->
                postSideEffect(TodaySafeWorkoutSideEffect.NavigateToExerciseDetail)
        }
    }
}
