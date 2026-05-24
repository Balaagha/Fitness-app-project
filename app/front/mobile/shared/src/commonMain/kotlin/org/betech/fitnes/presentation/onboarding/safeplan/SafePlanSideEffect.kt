package org.betech.fitnes.presentation.onboarding.safeplan

sealed interface SafePlanSideEffect {
    data object NavigateBack : SafePlanSideEffect
    data object NavigateToTodayWorkout : SafePlanSideEffect
    data object NavigateToPlanInfo : SafePlanSideEffect
}
