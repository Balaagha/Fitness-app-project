package org.betech.fitnes.presentation.onboarding.q4heightweight

/** Orbit intents for Q4. */
sealed interface Q4HeightWeightIntent {
    data class SetHeightCm(val cm: Int) : Q4HeightWeightIntent
    data class SetWeightKg(val kg: Int) : Q4HeightWeightIntent
    data class SetHeightUnit(val unit: HeightUnit) : Q4HeightWeightIntent
    data class SetWeightUnit(val unit: WeightUnit) : Q4HeightWeightIntent
    data class OpenDialog(val target: DialogTarget) : Q4HeightWeightIntent
    data object CloseDialog : Q4HeightWeightIntent
    data object BackTapped : Q4HeightWeightIntent
    data object Confirm : Q4HeightWeightIntent
}
