package org.betech.fitnes.presentation.onboarding.trimesterpostpartum

sealed interface TrimesterPostpartumIntent {
    data class Select(val choice: TrimesterChoice) : TrimesterPostpartumIntent
    data object BackTapped : TrimesterPostpartumIntent
    data object Confirm : TrimesterPostpartumIntent
}
