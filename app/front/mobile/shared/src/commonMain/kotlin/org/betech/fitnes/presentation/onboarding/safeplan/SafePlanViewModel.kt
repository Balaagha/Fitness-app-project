package org.betech.fitnes.presentation.onboarding.safeplan

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Safe 4-week Plan VM (Orbit MVI · Pencil o0BUd).
 *
 * NO repository dependencies — the plan is a curated static template baked
 * into the UI layer. NO AI plan generation trigger ever (CLAUDE.md hard-stop:
 * pregnancy_postpartum=true → curated static template only).
 *
 * State is local-only (selected day pill); navigation routes onward to the
 * TodaySafeWorkoutScreen (Pencil QHsnW) stub.
 */
class SafePlanViewModel :
    ViewModel(),
    ContainerHost<SafePlanState, SafePlanSideEffect> {

    override val container =
        container<SafePlanState, SafePlanSideEffect>(SafePlanState())

    fun onIntent(i: SafePlanIntent) = intent {
        when (i) {
            SafePlanIntent.BackTapped ->
                postSideEffect(SafePlanSideEffect.NavigateBack)
            SafePlanIntent.OpenTodayTapped ->
                postSideEffect(SafePlanSideEffect.NavigateToTodayWorkout)
            SafePlanIntent.OpenInfoTapped ->
                postSideEffect(SafePlanSideEffect.NavigateToPlanInfo)
            is SafePlanIntent.SelectDay ->
                reduce { state.copy(selectedDayIndex = i.index.coerceIn(0, 6)) }
        }
    }
}
