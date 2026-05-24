package org.betech.fitnes.presentation.onboarding.profilesummary

sealed interface ProfileSummarySideEffect {
    data object NavigateBack : ProfileSummarySideEffect

    /** Edit affordance — jump to the question screen for the given stepId. */
    data class NavigateToEdit(val stepId: String) : ProfileSummarySideEffect

    /** Post-create success → continue to AI Disclosure (Pencil eQcvv). */
    data object NavigateToAiDisclosure : ProfileSummarySideEffect

    data class ShowError(val message: String) : ProfileSummarySideEffect
}
