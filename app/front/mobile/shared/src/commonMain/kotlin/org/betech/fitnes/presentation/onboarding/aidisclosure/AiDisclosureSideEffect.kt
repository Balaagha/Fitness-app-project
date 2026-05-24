package org.betech.fitnes.presentation.onboarding.aidisclosure

sealed interface AiDisclosureSideEffect {
    data object NavigateBack : AiDisclosureSideEffect
    data object NavigateToAuthGate : AiDisclosureSideEffect
    data object ShowLearnMoreToast : AiDisclosureSideEffect
}
