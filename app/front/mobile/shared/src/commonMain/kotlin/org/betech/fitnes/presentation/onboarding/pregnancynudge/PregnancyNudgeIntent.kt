package org.betech.fitnes.presentation.onboarding.pregnancynudge

sealed interface PregnancyNudgeIntent {
    data object BackTapped : PregnancyNudgeIntent
    data object YesTapped : PregnancyNudgeIntent
    data object NoTapped : PregnancyNudgeIntent
}
