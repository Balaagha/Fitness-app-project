package org.betech.fitnes.presentation.onboarding.trimesterpostpartum

sealed interface TrimesterPostpartumSideEffect {
    data object NavigateBack : TrimesterPostpartumSideEffect
    data object NavigateToSafePlan : TrimesterPostpartumSideEffect
    data class ShowError(val message: String) : TrimesterPostpartumSideEffect
}
