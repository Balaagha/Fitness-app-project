package org.betech.fitnes.presentation.onboarding.profilesummary

import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

/**
 * Raw onboarding answers projection — keyed by stepId, value is the raw
 * value-string written by Q1..Q7 ViewModels (UI enum names for choices,
 * numeric strings for sliders / steppers).
 *
 * The Composable layer maps these raw values to localized display labels
 * via [LocalStrings] — keeping VM locale-agnostic.
 *
 * Known keys (in row order):
 *   q1_goal · q2_sex · q3_age · q4_height_cm · q4_weight_kg ·
 *   q5_experience · q6_context · q7_weekly_days · q7_session_minutes
 */
data class ProfileSummaryState(
    val answers: ImmutableMap<String, String> = persistentMapOf(),
    val isLoading: Boolean = true,
    val isCreating: Boolean = false,
)
