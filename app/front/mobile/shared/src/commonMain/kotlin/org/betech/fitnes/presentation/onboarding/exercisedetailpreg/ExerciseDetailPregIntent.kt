package org.betech.fitnes.presentation.onboarding.exercisedetailpreg

sealed interface ExerciseDetailPregIntent {
    data object BackTapped : ExerciseDetailPregIntent
    data object AlternativeTapped : ExerciseDetailPregIntent
    data object PlayTapped : ExerciseDetailPregIntent
    data object DoneTapped : ExerciseDetailPregIntent
    data object SkipTapped : ExerciseDetailPregIntent
    data object EasierTapped : ExerciseDetailPregIntent
}
