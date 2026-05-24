package org.betech.fitnes.presentation.onboarding.languageselect

/**
 * Supported UI languages — keep order = display order on the screen.
 * `toIso()` returns the 2-letter tag expected by [org.betech.fitnes.localization.resolveStrings].
 */
enum class LanguageCode {
    AZ, RU, EN;

    fun toIso(): String = name.lowercase()
}

data class LanguageSelectState(
    val selected: LanguageCode = LanguageCode.AZ,
    val isSaving: Boolean = false,
)
