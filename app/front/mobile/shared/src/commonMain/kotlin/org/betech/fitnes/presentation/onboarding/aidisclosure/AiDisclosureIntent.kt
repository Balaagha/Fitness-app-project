package org.betech.fitnes.presentation.onboarding.aidisclosure

sealed interface AiDisclosureIntent {
    data object BackTapped : AiDisclosureIntent
    data object ConfirmTapped : AiDisclosureIntent
    data object LearnMoreTapped : AiDisclosureIntent
}
