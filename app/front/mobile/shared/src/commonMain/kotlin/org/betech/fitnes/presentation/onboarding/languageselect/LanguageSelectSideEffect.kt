package org.betech.fitnes.presentation.onboarding.languageselect

sealed interface LanguageSelectSideEffect {
    data object NavigateToWelcome : LanguageSelectSideEffect
    data class ShowError(val message: String) : LanguageSelectSideEffect
}
