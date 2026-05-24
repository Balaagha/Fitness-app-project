package org.betech.fitnes.presentation.onboarding.exercisedetailpreg

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Exercise Detail (Pregnancy) VM (Orbit MVI · Pencil YZ38M).
 *
 * NO repository dependencies — exercise payload is a curated static template
 * baked into the UI layer. NO AI plan/exercise generation trigger ever
 * (CLAUDE.md hard-stop: pregnancy_postpartum=true → curated static template).
 *
 * - Back & Done both pop to TodaySafeWorkout list.
 * - Skip / Easier emit a transient ShowToast — no nav, no AI re-fetch.
 * - Alternative link is a visual swap affordance only (toast for now).
 */
class ExerciseDetailPregViewModel :
    ViewModel(),
    ContainerHost<ExerciseDetailPregState, ExerciseDetailPregSideEffect> {

    override val container =
        container<ExerciseDetailPregState, ExerciseDetailPregSideEffect>(ExerciseDetailPregState)

    fun onIntent(i: ExerciseDetailPregIntent) = intent {
        when (i) {
            ExerciseDetailPregIntent.BackTapped ->
                postSideEffect(ExerciseDetailPregSideEffect.NavigateBack)
            ExerciseDetailPregIntent.DoneTapped ->
                postSideEffect(ExerciseDetailPregSideEffect.NavigateDone)
            ExerciseDetailPregIntent.PlayTapped ->
                postSideEffect(ExerciseDetailPregSideEffect.ShowToast("play"))
            ExerciseDetailPregIntent.AlternativeTapped ->
                postSideEffect(ExerciseDetailPregSideEffect.ShowToast("alternative"))
            ExerciseDetailPregIntent.SkipTapped ->
                postSideEffect(ExerciseDetailPregSideEffect.ShowToast("skip"))
            ExerciseDetailPregIntent.EasierTapped ->
                postSideEffect(ExerciseDetailPregSideEffect.ShowToast("easier"))
        }
    }
}
