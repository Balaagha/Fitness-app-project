package org.betech.fitnes.presentation.onboarding.pregnancynudge

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Pregnancy Nudge VM (Orbit MVI) — Pencil M52XdD.
 *
 * Stateless prompt: ask whether the user is pregnant / postpartum so the next
 * step (PregnancyConfirm) can branch into the safe-template flow. This screen
 * itself is NOT a plan-AI trigger — it only routes navigation. The hard-stop
 * lives in [org.betech.fitnes.domain.usecase.EvaluatePregnancyHardStopUseCase],
 * invoked after the user confirms on the next screen.
 *
 * No repository deps — pure navigation VM.
 */
class PregnancyNudgeViewModel :
    ViewModel(), ContainerHost<PregnancyNudgeState, PregnancyNudgeSideEffect> {

    override val container =
        container<PregnancyNudgeState, PregnancyNudgeSideEffect>(PregnancyNudgeState)

    fun onIntent(i: PregnancyNudgeIntent) = intent {
        when (i) {
            PregnancyNudgeIntent.BackTapped ->
                postSideEffect(PregnancyNudgeSideEffect.NavigateBack)

            PregnancyNudgeIntent.YesTapped ->
                postSideEffect(PregnancyNudgeSideEffect.NavigateToPregnancyConfirm)

            PregnancyNudgeIntent.NoTapped ->
                postSideEffect(PregnancyNudgeSideEffect.NavigateBack)
        }
    }
}
