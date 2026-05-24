package org.betech.fitnes.presentation.onboarding.languageselect

sealed interface LanguageSelectIntent {
    data class Select(val code: LanguageCode) : LanguageSelectIntent
    data object Confirm : LanguageSelectIntent
}
