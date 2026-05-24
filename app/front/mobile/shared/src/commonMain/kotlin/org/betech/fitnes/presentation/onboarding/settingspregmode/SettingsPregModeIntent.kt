package org.betech.fitnes.presentation.onboarding.settingspregmode

/**
 * User-driven intents for the Settings · Pregnancy Mode screen (Pencil vUAuh).
 */
sealed interface SettingsPregModeIntent {
    data object BackTapped : SettingsPregModeIntent
    data object ChangePeriodTapped : SettingsPregModeIntent
    data object PostpartumTapped : SettingsPregModeIntent
    data object RemindersTapped : SettingsPregModeIntent
    data object DoctorNotesTapped : SettingsPregModeIntent
    data object DisableTapped : SettingsPregModeIntent
}
