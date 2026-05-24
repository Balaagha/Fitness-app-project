package org.betech.fitnes.presentation.onboarding.exercisedetailpreg

sealed interface ExerciseDetailPregSideEffect {
    /** Back chevron tap — pops to TodaySafeWorkout. */
    data object NavigateBack : ExerciseDetailPregSideEffect

    /** "Tamamladım" CTA — pop back to TodaySafeWorkout list. */
    data object NavigateDone : ExerciseDetailPregSideEffect

    /** Footer "Atla" / "Daha asan variant" — transient toast (no nav). */
    data class ShowToast(val message: String) : ExerciseDetailPregSideEffect
}
