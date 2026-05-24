package org.betech.fitnes.presentation.onboarding.profilesummary

sealed interface ProfileSummaryIntent {
    data object Load : ProfileSummaryIntent
    data object BackTapped : ProfileSummaryIntent
    data class EditRow(val stepId: String) : ProfileSummaryIntent
    data object CreateProfile : ProfileSummaryIntent
}
