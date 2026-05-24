package org.betech.fitnes.presentation.onboarding.settingspregmode

/**
 * Settings · Pregnancy Mode screen state (Pencil vUAuh).
 *
 * Static management surface — defaults match Pencil snapshot. Values are NOT
 * sourced from a repository in this iter (mock); when the real PregnancyMode
 * repository lands the static defaults move to the data layer.
 *
 * Invariant (CLAUDE.md): pregnancy_postpartum=true → AI plan generation
 * hard-stopped. This state never carries an AI trigger flag.
 */
data class SettingsPregModeState(
    val isActive: Boolean = true,
    val trimester: Int = 2,
    val weekCurrent: Int = 18,
    val weekTotal: Int = 40,
    val startedAtLabel: String = "17 yan 2026",
    val remindersOn: Boolean = true,
)
