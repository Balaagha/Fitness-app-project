package org.betech.fitnes.presentation.onboarding.q5experience

sealed interface Q5ExperienceIntent {
    data class Select(val choice: ExperienceChoice) : Q5ExperienceIntent
    data object BackTapped : Q5ExperienceIntent
    data object Confirm : Q5ExperienceIntent
}
