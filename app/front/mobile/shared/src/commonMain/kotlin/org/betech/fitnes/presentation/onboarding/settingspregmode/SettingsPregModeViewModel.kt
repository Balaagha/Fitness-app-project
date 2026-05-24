package org.betech.fitnes.presentation.onboarding.settingspregmode

import androidx.lifecycle.ViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

/**
 * Settings · Pregnancy Mode VM (Pencil vUAuh) — Orbit MVI.
 *
 * Static management surface: no repository wiring in iter 29. Reminders
 * toggle is reduced locally and emitted as a side effect so the future
 * PregnancyModeRepository can subscribe without UI changes.
 *
 * Invariant (CLAUDE.md): pregnancy_postpartum=true is a hard-stop on AI
 * plan generation; this VM contains zero AI trigger paths.
 */
class SettingsPregModeViewModel :
    ViewModel(),
    ContainerHost<SettingsPregModeState, SettingsPregModeSideEffect> {

    override val container =
        container<SettingsPregModeState, SettingsPregModeSideEffect>(SettingsPregModeState())

    fun onIntent(i: SettingsPregModeIntent) = intent {
        when (i) {
            SettingsPregModeIntent.BackTapped ->
                postSideEffect(SettingsPregModeSideEffect.NavigateBack)
            SettingsPregModeIntent.ChangePeriodTapped ->
                postSideEffect(SettingsPregModeSideEffect.NavigateToChangePeriod)
            SettingsPregModeIntent.PostpartumTapped ->
                postSideEffect(SettingsPregModeSideEffect.NavigateToPostpartum)
            SettingsPregModeIntent.RemindersTapped -> {
                val next = !state.remindersOn
                reduce { state.copy(remindersOn = next) }
                postSideEffect(SettingsPregModeSideEffect.ToggleReminders(next))
            }
            SettingsPregModeIntent.DoctorNotesTapped ->
                postSideEffect(SettingsPregModeSideEffect.NavigateToDoctorNotes)
            SettingsPregModeIntent.DisableTapped ->
                postSideEffect(SettingsPregModeSideEffect.ConfirmDisable)
        }
    }
}
