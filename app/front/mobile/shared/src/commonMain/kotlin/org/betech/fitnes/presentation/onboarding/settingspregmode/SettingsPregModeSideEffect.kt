package org.betech.fitnes.presentation.onboarding.settingspregmode

/**
 * Settings · Pregnancy Mode one-shot navigation effects (Pencil vUAuh).
 *
 * Destinations for the four management rows + destructive disable CTA. The
 * VM merely posts effects; the Screen owns the actual navigator transitions
 * (most destinations are not yet wired in Phase 3 — they pop back to safe
 * plan for now).
 */
sealed interface SettingsPregModeSideEffect {
    data object NavigateBack : SettingsPregModeSideEffect
    data object NavigateToChangePeriod : SettingsPregModeSideEffect
    data object NavigateToPostpartum : SettingsPregModeSideEffect
    data class ToggleReminders(val enabled: Boolean) : SettingsPregModeSideEffect
    data object NavigateToDoctorNotes : SettingsPregModeSideEffect
    data object ConfirmDisable : SettingsPregModeSideEffect
}
