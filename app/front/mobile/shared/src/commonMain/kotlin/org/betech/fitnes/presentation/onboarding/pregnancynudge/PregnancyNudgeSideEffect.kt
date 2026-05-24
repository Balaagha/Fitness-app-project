package org.betech.fitnes.presentation.onboarding.pregnancynudge

sealed interface PregnancyNudgeSideEffect {
    data object NavigateBack : PregnancyNudgeSideEffect
    data object NavigateToPregnancyConfirm : PregnancyNudgeSideEffect
}
