package org.betech.fitnes.presentation.onboarding.pregnancyconfirm

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Pregnancy Confirm VM (Orbit MVI) — Pencil pqupj.
 *
 * Static reassurance + branching screen. No AI plan trigger; the pregnancy
 * hard-stop is enforced upstream by [org.betech.fitnes.domain.usecase
 * .EvaluatePregnancyHardStopUseCase] whenever plan generation is attempted.
 *
 * Pure navigation VM — no repository deps.
 */
class PregnancyConfirmViewModel :
    ViewModel(), ContainerHost<PregnancyConfirmState, PregnancyConfirmSideEffect> {

    override val container =
        container<PregnancyConfirmState, PregnancyConfirmSideEffect>(PregnancyConfirmState)

    fun onIntent(i: PregnancyConfirmIntent) = intent {
        when (i) {
            PregnancyConfirmIntent.BackTapped ->
                postSideEffect(PregnancyConfirmSideEffect.NavigateBack)

            PregnancyConfirmIntent.ConfirmTapped ->
                postSideEffect(PregnancyConfirmSideEffect.NavigateToTrimester)
        }
    }
}
